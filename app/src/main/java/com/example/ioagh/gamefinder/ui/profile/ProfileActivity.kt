package com.example.ioagh.gamefinder.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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
import com.example.ioagh.gamefinder.ui.main.ChooseGameActivity
import com.example.ioagh.gamefinder.ui.main.SearchGameActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    private var drawer: DrawerLayout? = null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setNavigationViewListener()
        mAuth = FirebaseAuth.getInstance()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView: View = navigationView.inflateHeaderView(R.layout.nav_header)
        headerView.findViewById<TextView>(R.id.user_name_display).text = mAuth.currentUser!!.displayName!!

        nick.text = mAuth.currentUser!!.displayName
        email.text = mAuth.currentUser!!.email
        retrieveUserData(this)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            string.open_navigation_drawer, string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        initView()
    }

    private fun retrieveUserData(ctx: Context){
        usersReference.child(mAuth.currentUser!!.displayName!!).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                name.text = user.name
                age.text = if (user.age != null) user.age.toString() else ""
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        retrieveUserData(this)
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


        future_games_button.setOnClickListener(){
            intent = Intent(this, UserGameListActivity::class.java)
            intent.putExtra("username", mAuth.currentUser!!.displayName!!)
            intent.putExtra("gamekinds", NEXT_GAMES)
            startActivity(intent)
        }

        previous_games_button.setOnClickListener(){
            intent = Intent(this, UserGameListActivity::class.java)
            intent.putExtra("username", mAuth.currentUser!!.displayName!!)
            intent.putExtra("gamekinds", PREV_GAMES)
            startActivity(intent)
        }

        edit_image_button.setOnClickListener() {
            intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
