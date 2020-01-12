package com.example.ioagh.gamefinder.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.id
import com.example.ioagh.gamefinder.R.layout
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.ui.adapters.ChooseGameAdapter
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class ChooseGameActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: ChooseGameAdapter
    private lateinit var mAuth: FirebaseAuth

    private var drawer: DrawerLayout? = null

    private lateinit var gameName: String
    private lateinit var gameKind: MutableList<Int>
    private var minPlayers: Int? = null
    private var maxPlayers: Int? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var rangeInKm: Int? = null
    private lateinit var date: String
    private lateinit var owner: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_choose_game)

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

        gameName = intent.getStringExtra("gameName")
        gameKind = intent.getIntArrayExtra("gameKind").toMutableList()
        minPlayers = if (!intent.getStringExtra("minNumberOfPeople").isBlank())
            intent.getStringExtra("minNumberOfPeople").toInt()
        else
            null
        maxPlayers = if (!intent.getStringExtra("maxNumberOfPeople").isBlank())
            intent.getStringExtra("maxNumberOfPeople").toInt()
        else
            null

        longitude = if (!intent.getStringExtra("longitude").isBlank()&& intent.getStringExtra("longitude") != "null")
            intent.getStringExtra("longitude").toDouble()
        else
            null
        latitude = if (!intent.getStringExtra("latitude").isBlank() && intent.getStringExtra("latitude") != "null")
            intent.getStringExtra("latitude").toDouble()
        else
            null
        rangeInKm = if (!intent.getStringExtra("range").isBlank()&& intent.getStringExtra("range") != "null")
            intent.getStringExtra("range").toInt()
        else
            null

        date = intent.getStringExtra("date")
        owner = intent.getStringExtra("owner")

        setNavigationViewListener()
        retrieveData(this)
    }


    private fun retrieveData(ctx: Context) {
        val packageContext = this
        gamesReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val list : ArrayList<GameViewModel> = ArrayList()
                for(data in p0.children) {
                    if (data != null) {
                        val game = data.getValue(Game::class.java)!!
                        val hashCode = data.key!!
                        if (validate(game)) {
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
        if ((gameName.isBlank() ||game.gameName!!.contains(gameName)) &&
            (gameKind.isEmpty() || (game.gameTypes != null && game.gameTypes!!.containsAll(gameKind))) &&
                    //game.date == date &&
            (minPlayers == null || game.maxPlayers!! >= minPlayers!!) &&
            (maxPlayers == null || game.maxPlayers!! <= maxPlayers!!) &&
            (owner.isBlank() || game.owner == owner)) {

            if (rangeInKm == null) {
                return true
            } else if (longitude != null && latitude != null) {
                return getDistanceFromLatLonInKm(latitude!!, longitude!!, game.latitude!!, game.longitude!!) < rangeInKm!!
            } else {
                return true
            }
        }
        return false
    }

    private fun getDistanceFromLatLonInKm(lat1 : Double,lon1 : Double ,lat2 : Double,lon2 : Double) : Double {
        val r = 6371 // Radius of the earth in km
        val dLat = deg2rad(lat2-lat1)  // deg2rad below
        val dLon = deg2rad(lon2-lon1)
        val a =
            sin(dLat/2) * sin(dLat/2) +
                    cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                    sin(dLon/2) * sin(dLon/2)

        val c = 2 * atan2(sqrt(a), sqrt(1-a))
        val d = r * c; // Distance in km
        return d;
    }

    private fun deg2rad(deg : Double) : Double {
        return deg * (Math.PI/180)
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
