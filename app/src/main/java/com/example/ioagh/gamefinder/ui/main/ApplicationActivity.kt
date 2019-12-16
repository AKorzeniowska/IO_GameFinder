package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_application.*

class ApplicationActivity : AppCompatActivity() {

    private var drawer: DrawerLayout? = null

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_application)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            string.open_navigation_drawer, string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()

        initView()
    }

    private fun initView() {
        searchGameButton.setOnClickListener() {
            val searchGameActivity = Intent(this, SearchGameActivity::class.java)
            startActivity(searchGameActivity)
        }

        addGameButton.setOnClickListener() {
            val intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

        openChatButton.setOnClickListener() {
            val intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }

        userProfileButton.setOnClickListener() {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            mAuth.signOut()
            startActivity(intent)
            finish()
        }

        sample_game.setOnClickListener(){
            val intent = Intent(this, PickedGameActivity::class.java)
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

