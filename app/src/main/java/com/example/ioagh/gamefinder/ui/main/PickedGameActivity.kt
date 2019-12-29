package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.providers.addGameToJoined
import com.example.ioagh.gamefinder.providers.decreaseGamersNumber
import com.example.ioagh.gamefinder.providers.setViewIfUserJoinedGame
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_picked_game.*

class PickedGameActivity : AppCompatActivity() {

    private var drawer: DrawerLayout? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var gameId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picked_game)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.open_navigation_drawer, R.string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()
        gameId = intent.getStringExtra("gameHash")
        initView()
    }

    private fun initView(){
        setViewIfUserJoinedGame(gameId, mAuth.currentUser?.displayName.toString(), joinGameButton)

        joinGameButton.setOnClickListener {
            decreaseGamersNumber(gameId)
            addGameToJoined(gameId, mAuth.currentUser?.displayName.toString())
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatroom", gameId)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }
}
