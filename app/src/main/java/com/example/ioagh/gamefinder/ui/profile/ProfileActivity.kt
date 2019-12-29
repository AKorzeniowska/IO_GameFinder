package com.example.ioagh.gamefinder.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.main.ChooseGameActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    private var drawer: DrawerLayout? = null
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_profile)

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
        initView()
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

    private fun initView(){
        my_games_button.setOnClickListener() {
                intent = Intent(this, ChooseGameActivity::class.java)
                intent.putExtra("gameName", "")
                intent.putExtra("gameKind", "")
                intent.putExtra("minNumberOfPeople", "")
                intent.putExtra("maxNumberOfPeople", "")
                intent.putExtra("localization", "")
                intent.putExtra("date", "")
                intent.putExtra("owner", mAuth.currentUser!!.displayName!!)
                startActivity(intent)
        }
    }
}
