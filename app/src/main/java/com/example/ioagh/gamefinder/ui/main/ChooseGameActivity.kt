package com.example.ioagh.gamefinder.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChooseGameActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    private lateinit var recyclerView : RecyclerView

    private lateinit var gameName: String
    private  var gameKind: String = ""
    private  var minPlayers: Int = 0
    private  var maxPlayers: Int = 0
    private lateinit var localization: String
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_choose_game)
        gameName = intent.getStringExtra("gameName")
        gameKind = intent.getStringExtra("gameKind")
        minPlayers = intent.getStringExtra("minNumberOfPeople").toInt()
        maxPlayers = intent.getStringExtra("maxNumberOfPeople").toInt()
        localization = intent.getStringExtra("localization")
        date = intent.getStringExtra("date")

        retrieveData(this)
    }

    fun retrieveData(ctx: Context) {
        databaseReference.child("games").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list : ArrayList<Game> = ArrayList()
                for(data in p0.children) {
                    if (data != null) {
                        val game = data.getValue(Game::class.java)!!
                        if (validate(game)) {
                            list.add(game)
                        }
                    }
                }
                recyclerView = findViewById(id.chooseGameRecyclerView)
                recyclerView.adapter = ChooseGameAdapter(list, ctx)
                recyclerView.layoutManager = LinearLayoutManager(ctx)
                recyclerView.setHasFixedSize(true)
            }

        })
    }

    fun validate(game: Game): Boolean {
        if (game.gameName!!.contains(gameName) &&
            (gameKind.isBlank() || gameKind.toInt() == game.gameKind) &&
            (localization.isBlank() || game.localization!!.contains(localization)) &&
                    //game.date == date &&
                    game.players!! >= minPlayers && game.players!! <= maxPlayers) {
            return true
        }
        return false
    }

}
