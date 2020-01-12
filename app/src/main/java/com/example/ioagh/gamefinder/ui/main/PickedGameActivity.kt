package com.example.ioagh.gamefinder.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import com.example.ioagh.gamefinder.providers.*
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_game.*
import kotlinx.android.synthetic.main.activity_picked_game.*

class PickedGameActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    private var drawer: DrawerLayout? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var gameId: String
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picked_game)

//        gameDescription.visibility = View.GONE
//        gameDescriptionTextView.visibility = View.GONE

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.open_navigation_drawer, R.string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView: View = navigationView.inflateHeaderView(R.layout.nav_header)
        headerView.findViewById<TextView>(R.id.user_name_display).text = mAuth.currentUser!!.displayName!!

        gameId = intent.getStringExtra("gameHash")
        initView()
        setNavigationViewListener()
        retrieveBookData(gameId, this)
    }

    override fun onResume() {
        super.onResume()
        initView()
        retrieveBookData(gameId, this)
    }

    private fun retrieveBookData(gameId: String, ctx: Context) {
        gamesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list : ArrayList<GameViewModel> = ArrayList()
                for(data in p0.children) {
                    if (data != null) {
                        val hashCode = data.key!!
                        if (hashCode == gameId){
                            game = data.getValue(Game::class.java)!!
                            setGameView(game)
                            getGameTypes(ctx)
                        }
                    }
                }
            }

        })
    }

    private fun setGameView(game: Game) {
        gameName.text = game.gameName
        //gameLocalization.text = game.localization
        predictedGameTime.text = parseMinutesToSring(game.durationInMinutes!!)
        currentNumberOfPlayers.text = game.players.toString()
        maximumNumberOfPlayers.text = game.maxPlayers.toString()
    }

    private fun initView(){
        setViewIfUserJoinedGame(gameId, mAuth.currentUser?.displayName.toString(), joinGameButton)


        joinGameButton.setOnClickListener {
            increaseGamersNumber(gameId)
            addGameToJoined(gameId, mAuth.currentUser?.displayName.toString())
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatroom", gameId)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

    private fun getGameTypes(ctx: Context){
        var inputString = ""
        gameTypesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    if (game.gameTypes!!.contains(data.key!!.toInt())) {
                        inputString += data.getValue(String::class.java) + ", "
                    }
                }
               // gameKinds.text = inputString.substring(0, inputString.length - 2)
            }
        })
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
