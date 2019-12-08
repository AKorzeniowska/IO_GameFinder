package com.example.ioagh.gamefinder.ui.main

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.MemberData
import com.example.ioagh.gamefinder.models.Message
import com.example.ioagh.gamefinder.ui.adapters.MessageAdapter
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.scaledrone.lib.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*


class ChatActivity : RoomListener, AppCompatActivity() {

    private val channelID = "odGUUSff4QpqU8tt"
    private var roomName: String? = null
    private lateinit var editText: EditText
    private lateinit var scaledrone: Scaledrone
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messagesView: ListView
    private lateinit var mAuth: FirebaseAuth
    private val roomNamePrefix = "observable-"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatSendButton.visibility = View.INVISIBLE
        chatSendButton.isEnabled = false
        roomName = intent.extras.getString("chatroom")
        roomName = roomNamePrefix + roomName

        editText = findViewById(R.id.editText)
        messageAdapter = MessageAdapter(this)
        messagesView = findViewById(R.id.messages_view)
        messagesView.adapter = messageAdapter

        mAuth = FirebaseAuth.getInstance()
        startScaleDroneChat()
    }

    override fun onOpen(room: Room?) {
        println("Connected to room")
    }

    override fun onOpenFailure(room: Room?, ex: Exception?) {
        println(ex)
    }

    override fun onMessage(room: Room?, receivedMessage: com.scaledrone.lib.Message) {
        val mapper = ObjectMapper()
        try {
            val data =
                mapper.treeToValue(
                    receivedMessage.member?.clientData,
                    MemberData::class.java
                )
            val belongsToCurrentUser =
                receivedMessage.clientID == scaledrone.clientID
            val message =
                Message(receivedMessage.data.asText(), data, belongsToCurrentUser)
            runOnUiThread {
                messageAdapter.add(message)
                messagesView.setSelection(messagesView.count - 1)
            }
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
    }

    private fun parseMessage(receivedMessage: com.scaledrone.lib.Message){
        try {
            val data: MemberData = MemberData("", getRandomColor())
            val belongsToCurrentUser =
                receivedMessage.clientID == scaledrone.clientID
            val message =
                Message(receivedMessage.data.asText(), data, belongsToCurrentUser)
            runOnUiThread {
                messageAdapter.add(message)
                messagesView.setSelection(messagesView.count - 1)
            }
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
    }

    private fun startScaleDroneChat(){
        val nick: String
        if (mAuth.currentUser?.displayName != null) {
            nick = mAuth.currentUser!!.displayName!!
        }
        else{
            nick = "nazwa_usera"
        }
        val data: MemberData = MemberData(nick, getRandomColor())
        scaledrone = Scaledrone(channelID, data)

        scaledrone.connect(object : Listener {
            override fun onOpen() {
                println("Scaledrone - connected to the room")
                val chatroom = scaledrone.subscribe(roomName, this@ChatActivity, SubscribeOptions(50))

                chatroom.listenToHistoryEvents { room, message ->
                    parseMessage(message)
                }
                chatSendButton.isEnabled = true
                chatSendButton.visibility = View.VISIBLE
            }

            override fun onOpenFailure(ex: java.lang.Exception?) {
                System.err.println(ex)
            }

            override fun onFailure(ex: java.lang.Exception?) {
                System.err.println(ex)
            }

            override fun onClosed(reason: String?) {
                System.err.println(reason)
            }
        })
    }

    fun sendMessage(view: View?) {
        val message = editText.text.toString()
        if (message.isNotEmpty()) {
            scaledrone.publish(roomName, message)
            editText.text.clear()
        }
    }

    private fun getRandomColor(): String{
        val r = Random()
        val sb = StringBuffer("#")
        while(sb.length < 7){
            sb.append(java.lang.Integer.toHexString(r.nextInt()))
        }
        return sb.toString().substring(0,7)
    }
}
