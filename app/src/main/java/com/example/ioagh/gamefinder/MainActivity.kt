package com.example.ioagh.gamefinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.LoginActivity
import com.example.ioagh.gamefinder.ui.RegisterActivity
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        initView()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if (currentUser != null){
            val appActivity = Intent(this, ApplicationActivity::class.java)
            startActivity(appActivity)
            finish()
        }
    }

    private fun initView() {
        loginButton.setOnClickListener {
            val loginPage = Intent(this, LoginActivity::class.java)
            startActivity(loginPage)
            //finish()
        }

        registerButton.setOnClickListener {
            val registerPage = Intent(this, RegisterActivity::class.java)
            startActivity(registerPage)
            //finish()
        }
    }
}