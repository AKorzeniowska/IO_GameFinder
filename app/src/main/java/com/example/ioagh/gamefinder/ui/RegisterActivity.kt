package com.example.ioagh.gamefinder.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ioagh.gamefinder.R.*
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
            //oAuth register
            //val intent = Intent(this, <some class>
            //startActivity(intent)
            //finish()
            Toast.makeText(this, "To be implemented", Toast.LENGTH_LONG).show()
        }
    }
}