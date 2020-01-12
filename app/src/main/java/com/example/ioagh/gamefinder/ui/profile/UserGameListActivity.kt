package com.example.ioagh.gamefinder.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.*
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import com.example.ioagh.gamefinder.ui.main.AddGameActivity
import com.example.ioagh.gamefinder.ui.main.ChatListActivity
import com.example.ioagh.gamefinder.ui.main.PickedGameActivity
import com.example.ioagh.gamefinder.ui.main.SearchGameActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserGameListActivity : NavigationView.OnNavigationItemSelectedListener,  AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: ChooseGameAdapter

    private var drawer: DrawerLayout? = null
    private lateinit var mAuth: FirebaseAuth


    private lateinit var userName: String
    private var gameKinds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_choose_game)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setNavigationViewListener()
        mAuth = FirebaseAuth.getInstance()

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.open_navigation_drawer, R.string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        userName = intent.getStringExtra("username")
        gameKinds = intent.getIntExtra("gamekinds", 0)

        retrieveUserData(this, userName)
    }

    private fun retrieveUserData(ctx: Context, username: String){
        var usersGamesHashes = ArrayList<String>()

        usersReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>(User::class.java)
                val createdGameList = user?.createdGames as ArrayList<String>
                val joinedGameList = user.joinedGames as ArrayList<String>

                usersGamesHashes.addAll(createdGameList)
                usersGamesHashes.addAll(joinedGameList)

                retrieveData(ctx, usersGamesHashes)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }


    private fun retrieveData(ctx: Context, gameHashes: List<String>) {
        val packageContext = this
        gamesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list : ArrayList<GameViewModel> = ArrayList()
                for(data in p0.children) {
                    if (data != null) {
                        val game = data.getValue(Game::class.java)!!
                        val hashCode = data.key!!
                        if (gameHashes.contains(hashCode) && validate(game)) {
                            list.add(GameViewModel(game, hashCode))
                        }
                    }
                }
                adapter = ChooseGameAdapter(list, ctx)
                recyclerView = findViewById(id.chooseGameRecyclerView)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(ctx)
                recyclerView.setHasFixedSize(true)
                adapter.onItemClick = { game ->
                    val intent = Intent(packageContext, PickedGameActivity::class.java)
                    intent.putExtra("gameHash", game.gameHash)
                    startActivity(intent)
                }
            }

        })
    }


    fun validate(game: Game): Boolean {
        if (gameKinds == PREV_GAMES && getTodaysDate().compareTo(parseStringToDate(game.date!!)) >= 0) {
            return true
        }
        else if (gameKinds == NEXT_GAMES && getTodaysDate().compareTo(parseStringToDate(game.date!!)) < 0) {
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
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

const val PREV_GAMES = 1
const val NEXT_GAMES = 2