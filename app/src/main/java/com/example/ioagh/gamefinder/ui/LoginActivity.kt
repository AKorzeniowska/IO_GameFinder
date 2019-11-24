package com.example.ioagh.gamefinder.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.widget.Toast

import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        initView()
    }

    private fun initView() {
        emailLogin.hint = "Enter an email"
        passwordLogin.hint = "Enter a password"
        loginActivityButton.setOnClickListener {

            signIn(emailLogin.text.toString(), passwordLogin.text.toString())
        }
    }

    private fun signIn(email: String, password: String) {
        if (emailLogin.text.toString() == "" || passwordLogin.text.toString() == "" ) {
            Toast.makeText(
                this, "Podaj dane logowania",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener <AuthResult>() {
                @Override
                fun onComplete(@NonNull task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        var user: FirebaseUser? = mAuth.currentUser
                        val appActivity = Intent(this, ApplicationActivity::class.java)
                        startActivity(appActivity)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this, "Niepoprawne dane logowania",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}
