package com.example.ioagh.gamefinder.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_register)

        initView()
    }

    private fun initView() {
        emailRegister.hint = "Enter an email"
        passwordRegister.hint = "password"
        passwordRegisterRepeat.hint = "retype password"
        registerActivityButton.setOnClickListener() {
            //TODO  register verification by mOAuth
            val intent = Intent(this, ApplicationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}