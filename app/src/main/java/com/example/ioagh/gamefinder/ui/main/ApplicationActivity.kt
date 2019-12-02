package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_application.*

class ApplicationActivity : AppCompatActivity() {

    companion object DatabaseInfoProvider {
        const val JDBC_DRIVER = ""
        const val URL = "sql7.freesqldatabase.com"
        const val USER = "sql7311651"
        const val PASSWORD = "cQwhQGVu9w"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_application)

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
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        userProfileButton.setOnClickListener() {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}

