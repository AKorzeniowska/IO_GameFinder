package com.example.ioagh.gamefinder.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.ioagh.gamefinder.R.attr
import com.example.ioagh.gamefinder.R.layout
import com.example.ioagh.gamefinder.providers.createUser
import com.example.ioagh.gamefinder.providers.userExists
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        initView()
    }

    private fun initView() {
        emailRegister.hint = "podaj e-mail"
        passwordRegister.hint = "podaj hasło"
        passwordRegisterRepeat.hint = "powtórz hasło"
        nickRegister.hint = "podaj nazwę użytkownika"
        registerActivityButton.setOnClickListener() {
            createAccount(emailRegister.text.toString(),
                passwordRegister.text.toString(),
                passwordRegisterRepeat.text.toString(),
                nickRegister.text.toString())
        }
    }

    private fun createAccount(email: String, password: String, passwordRepeat: String, nick: String) {
        if (password != passwordRepeat){
            Toast.makeText(
                this, "Podane hasła są różne",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password == "" || email == "" || nick == ""){
            Toast.makeText(
                this, "Podaj dane do rejestracji",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val regex = "^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()){
            Toast.makeText(
                this, "Błędny adres e-mail",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    Toast.makeText(
                        this, "Zarejestrowano!",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (userExists(nick)){
                        createUser(nick)
                    }

                    val appActivity = Intent(this, ApplicationActivity::class.java)
                    startActivity(appActivity)
                    finish()

                } else { // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this, "Błąd podczas rejestracji",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}