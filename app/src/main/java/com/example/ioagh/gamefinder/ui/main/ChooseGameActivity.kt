package com.example.ioagh.gamefinder.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.R.id
import com.example.ioagh.gamefinder.R.layout
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChooseGameActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: ChooseGameAdapter

    private lateinit var gameName: String
    private lateinit var gameKind: String
    private var minPlayers: Int? = null
    private var maxPlayers: Int? = null
    private lateinit var localization: String
    private lateinit var date: String
    private lateinit var owner: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_choose_game)

        gameName = intent.getStringExtra("gameName")
        gameKind = intent.getStringExtra("gameKind")
        localization = intent.getStringExtra("minNumberOfPeople")
        minPlayers = if (!intent.getStringExtra("minNumberOfPeople").isBlank())
            intent.getStringExtra("minNumberOfPeople").toInt()
        else
            null
        maxPlayers = if (!intent.getStringExtra("maxNumberOfPeople").isBlank())
            intent.getStringExtra("maxNumberOfPeople").toInt()
        else
            null
        localization = intent.getStringExtra("localization")
        date = intent.getStringExtra("date")
        owner = intent.getStringExtra("owner")

        retrieveData(this)
    }


    fun retrieveData(ctx: Context) {
        val packageContext = this
        gamesReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list : ArrayList<GameViewModel> = ArrayList()
                for(data in p0.children) {
                    if (data != null) {
                        val game = data.getValue(Game::class.java)!!
                        val hashCode = data.key!!
                        if (validate(game)) {
                            list.add(GameViewModel(game, hashCode))
                        }
                    }
                }
                adapter = ChooseGameAdapter(list, ctx)
                recyclerView = findViewById(id.chooseGameRecyclerView)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(ctx)
                recyclerView.setHasFixedSize(true)
                adapter.onItemClick = { game ->
                    val intent = Intent(packageContext, PickedGameActivity::class.java)
                    intent.putExtra("gameHash", game.gameHash)
                    startActivity(intent)
                }
            }

        })
    }


    fun validate(game: Game): Boolean {
        if ((gameName.isBlank() ||game.gameName!!.contains(gameName)) &&
            (gameKind.isBlank() || gameKind.toInt() == game.gameKind) &&
            (localization.isBlank() || game.localization!!.contains(localization)) &&
                    //game.date == date &&
            (minPlayers == null || game.maxPlayers!! >= minPlayers!!) &&
            (maxPlayers == null || game.maxPlayers!! <= maxPlayers!!) &&
            (owner.isBlank() || game.owner == owner)) {
            return true
        }
        return false
    }

}
