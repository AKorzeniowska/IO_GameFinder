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
import kotlinx.android.synthetic.main.activity_choose_game.*


class ChooseGameActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_choose_game)

        retrieveData(this)
    }

    fun retrieveData(ctx: Context) {
        val list : ArrayList<Game> = ArrayList()
        databaseReference.child("games").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children) {
                    if (data != null) {
                        list.add(data.getValue(Game::class.java)!!)
                    }
                }
                recyclerView = findViewById(id.chooseGameRecyclerView)
                recyclerView.adapter = ChooseGameAdapter(list, ctx)
                recyclerView.layoutManager = LinearLayoutManager(ctx)
                recyclerView.setHasFixedSize(true)
            }

        })
    }
}
