package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.Toast
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.Manifest
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.layout
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_application.*
//import kotlinx.android.synthetic.main.activity_application.searchGameButton
import kotlinx.android.synthetic.main.activity_search_game.*
import kotlinx.android.synthetic.main.nav_header.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ApplicationActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var map: GoogleMap

    private var drawer: DrawerLayout? = null

    lateinit var mAuth: FirebaseAuth
    private lateinit var location: Location
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private val REQUEST_CODE : Int = 101
    private val games: HashMap<String, String> = HashMap()

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference
    val gamesReference = databaseReference.child("games")
    var game : Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_application)

        setNavigationViewListener()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLastLocation()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //nav_display_name.text = "Zalogowany jako: " + mAuth.currentUser!!.displayName!!
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        mAuth = FirebaseAuth.getInstance()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView: View = navigationView.inflateHeaderView(R.layout.nav_header)
        headerView.findViewById<TextView>(R.id.user_name_display).text = mAuth.currentUser!!.displayName!!
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.open_navigation_drawer, R.string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,  arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }
        var task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                location = it
                val supportMapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                map.isMyLocationEnabled = true
                supportMapFragment.getMapAsync(this@ApplicationActivity)
                val ny = LatLng(location.latitude, location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLng(ny))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 14.0f))
            }
        }
        task.addOnFailureListener { Log.e(it.toString(), "ERROR") }
    }

     override fun onMapReady(googleMap: GoogleMap) {
         map = googleMap
         addPinsToMap()
         map.setOnInfoWindowClickListener(this)
    }

    private fun addPinsToMap() {
        gamesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val gamePin = data.getValue(Game::class.java) as Game
                    val marker = MarkerOptions().position(LatLng(gamePin.latitude as Double, gamePin.longitude as Double))
                    marker.title("Nazwa gry: " + gamePin.gameName)
                    marker.snippet("Ilość graczy: " + gamePin.players + "/" + gamePin.maxPlayers)
                    map.addMarker(marker)
                    data!!.key?.let { games.put("Nazwa gry: " + gamePin.gameName, it) }
                }
            }
        })
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

    override fun onInfoWindowClick(marker: Marker?) {
        val string = games.get(marker!!.title)
        Log.d(string, string)
        val intent = Intent(this, PickedGameActivity::class.java)
        intent.putExtra("gameHash", string)
        startActivity(intent)
    }
}

