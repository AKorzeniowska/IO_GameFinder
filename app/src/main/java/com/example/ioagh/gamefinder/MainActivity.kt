package com.example.ioagh.gamefinder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.LoginActivity
import com.example.ioagh.gamefinder.ui.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        //TODO check if user is already signed in, when it's true then proceed to main app

        initView()
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