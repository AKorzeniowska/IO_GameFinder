package com.example.ioagh.gamefinder.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.ChatViewModel
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.adapters.ChatAdapter
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatListActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        mAuth = FirebaseAuth.getInstance()
        retrieveUserData(this, mAuth.currentUser!!.displayName!!)
    }

    fun retrieveUserData(ctx: Context, username: String){
        var usersGamesHashes = ArrayList<String>()

        usersReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>(User::class.java)
                val createdGameList = user?.createdGames as ArrayList<String>
                val joinedGameList = user.joinedGames as ArrayList<String>

                usersGamesHashes.addAll(createdGameList)
                usersGamesHashes.addAll(joinedGameList)

                retrieveData(ctx, usersGamesHashes)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun retrieveData(ctx: Context, gameHashes: List<String>) {
        gamesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list : ArrayList<ChatViewModel> = ArrayList()
                for(data in p0.children) {
                    if (data != null) {
                        val hashCode = data.key!!
                        if (gameHashes.contains(hashCode)) {
                            val game = data.getValue(Game::class.java)!!
                            list.add(ChatViewModel(hashCode, game.gameName!!, game.date!!))
                        }
                    }
                }
                adapter = ChatAdapter(list, ctx)
                recyclerView = findViewById(R.id.chatListRecyclerView)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(ctx)
                recyclerView.setHasFixedSize(true)
                adapter.onItemClick = { game ->
                    val intent = Intent(ctx, ChatActivity::class.java)
                    intent.putExtra("chatroom", game.gameHash)
                    startActivity(intent)
                }
            }

        })
    }

}
