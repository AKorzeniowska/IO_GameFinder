package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.layout
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_application.*
import kotlinx.android.synthetic.main.activity_application.searchGameButton
import kotlinx.android.synthetic.main.activity_search_game.*
import kotlinx.android.synthetic.main.nav_header.*


class ApplicationActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_application)

        setNavigationViewListener()

        //nav_display_name.text = "Zalogowany jako: " + mAuth.currentUser!!.displayName!!
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

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_chat -> {
                intent = Intent(this, ChatListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_search -> {
                intent = Intent(this, SearchGameActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_my_games -> {
                intent = Intent(this, ChooseGameActivity::class.java)
                intent.putExtra("gameName", "")
                intent.putExtra("gameKind", "")
                intent.putExtra("minNumberOfPeople", "")
                intent.putExtra("maxNumberOfPeople", "")
                intent.putExtra("localization", "")
                intent.putExtra("date", "")
                intent.putExtra("owner", mAuth.currentUser!!.displayName!!)
                startActivity(intent)
            }
            R.id.nav_joined_games -> {

            }
            R.id.nav_logout -> {
                mAuth.signOut()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    private fun setNavigationViewListener() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }
}

