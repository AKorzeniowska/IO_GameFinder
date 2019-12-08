package com.example.ioagh.gamefinder.ui.main

import android.os.Bundle
import android.util.LayoutDirection
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import kotlinx.android.synthetic.main.activity_choose_game.*

class SearchGameActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private var games: Array<String> = arrayOf("game1", "game2", "game3", "game4", "game5", "game6","game7","game8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search_game)
        recyclerView = this.findViewById(id.chooseGameRecyclerView)
        recyclerView.adapter = ChooseGameAdapter(games, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

}
