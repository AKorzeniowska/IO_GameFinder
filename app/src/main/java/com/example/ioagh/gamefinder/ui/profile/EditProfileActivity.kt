package com.example.ioagh.gamefinder.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.main.AddGameActivity
import com.example.ioagh.gamefinder.ui.main.ChatListActivity
import com.example.ioagh.gamefinder.ui.main.SearchGameActivity
import com.example.ioagh.gamefinder.validators.EmailValidator
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    private var drawer: DrawerLayout? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_edit_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            string.open_navigation_drawer, string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView: View = navigationView.inflateHeaderView(R.layout.nav_header)
        headerView.findViewById<TextView>(R.id.user_name_display).text = mAuth.currentUser!!.displayName!!

        setNavigationViewListener()
        retrieveUserData(this)

        acceptChanges.setOnClickListener(){
            updateUser()
            Toast.makeText(this, "Pomyślnie zapisano zmiany", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

    private fun retrieveUserData(ctx: Context){
        usersReference.child(mAuth.currentUser!!.displayName!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)!!
                setView(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    private fun setView(user: User){
        nameEdit.setText(user.name)
        emailEdit.setText(mAuth.currentUser!!.email)
    }

    private fun updateUser(){
        if (nameEdit.text.toString() != user.name){
            user.name = nameEdit.text.toString()
            usersReference.child(mAuth.currentUser!!.displayName!!).setValue(user)
        }
        if (emailEdit.text.toString() != mAuth.currentUser!!.email){
            if (validateEmail(mAuth.currentUser!!.email!!)) {
                mAuth.currentUser!!.updateEmail(emailEdit.text.toString())
            }
        }
        if (passwordEdit.text.toString() != ""){
            if (validatePasswords(passwordEdit.text.toString(), confirmPasswordEdit.text.toString())){
                mAuth.currentUser!!.updatePassword(passwordEdit.text.toString())
            }
        }
    }

    private fun validatePasswords(password: String, passwordRepeat: String): Boolean{
        if (password.length < 6 && password != "" && passwordRepeat != ""){
            Toast.makeText(this, "hasło zbyt krótkie (minimum 6 znaków)", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != passwordRepeat){
            Toast.makeText(this, "podane hasła są różne", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateEmail(email: String): Boolean{
        if (!EmailValidator.validate(email)){
            Toast.makeText(
                this, R.string.invalid_email_address,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_chat -> {
                intent = Intent(this, ChatListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_search_game -> {
                intent = Intent(this, SearchGameActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_add_game -> {
                intent = Intent(this, AddGameActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
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
