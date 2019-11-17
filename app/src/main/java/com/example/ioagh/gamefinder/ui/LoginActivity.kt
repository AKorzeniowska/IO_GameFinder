package com.example.ioagh.gamefinder.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        initView()
    }

    private fun initView() {
        emailLogin.hint = "Enter an email"
        passwordLogin.hint = "Enter a password"
        loginActivityButton.setOnClickListener() {
            //TODO check if email and password are valid
            val intent = Intent(this, ApplicationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
