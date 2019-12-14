package com.example.ioagh.gamefinder.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.createUser
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        initView()
    }

    private fun initView() {
        emailRegister.hint = "podaj e-mail"
        passwordRegister.hint = "podaj hasło"
        passwordRegisterRepeat.hint = "powtórz hasło"
        nickRegister.hint = "podaj nazwę użytkownika"
        registerActivityButton.setOnClickListener {
            createAccount(emailRegister.text.toString(),
                passwordRegister.text.toString(),
                passwordRegisterRepeat.text.toString(),
                nickRegister.text.toString(),
                nameRegister.text.toString(),
                ageRegister.text.toString())
        }
    }

    private fun createAccount(email: String, password: String, passwordRepeat: String, nick: String, name: String, age: String) {
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

        val regex = "^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z0-9.-]+\$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()){
            Toast.makeText(
                this, "Błędny adres e-mail",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val context = this
        usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(nick)) {
                    Toast.makeText(
                        context, R.string.user_already_exists,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    createUser(email, password, nick, age, name)
                }
            }
        })
}

private fun createUser(email: String, password: String, nick: String, age:String, name:String){
    progressBar.visibility = View.VISIBLE
    mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                val user = mAuth.currentUser
                val profileUpdates =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(nick)
                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build()

                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dbuser = User(age.toInt(), name)
                            createUser(this, dbuser, nick)
                            val appActivity = Intent(this, ApplicationActivity::class.java)
                            startActivity(appActivity)
                            finish()
                        }
                    }

            } else {
                Toast.makeText(
                    this, "Błąd podczas rejestracji",
                    Toast.LENGTH_SHORT
                ).show()
            }
            progressBar.visibility = View.INVISIBLE
        }
}
}