package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.R.layout
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


class ApplicationActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private var drawer: DrawerLayout? = null

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_application)

        setNavigationViewListener()


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

        initView()
       // initUI()
    }

//    fun initUI() {
//        mapView?.postDelayed({
//            mapView?.onCreate(Bundle())
//            mapView?.getMapAsync {
//                onMapReady(it)
//            }
//        }, 500)
//    }

     override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
//        map?.setMinZoomPreference(14.0f)
        val ny = LatLng(40.7143528, -74.0059731)
        map?.addMarker(MarkerOptions().position(ny));
        map?.moveCamera(CameraUpdateFactory.newLatLng(ny))
    }



    private fun initView() {
//        searchGameButton.setOnClickListener() {
//            val searchGameActivity = Intent(this, SearchGameActivity::class.java)
//            startActivity(searchGameActivity)
//        }

//        addGameButton.setOnClickListener() {
//            val intent = Intent(this, AddGameActivity::class.java)
//            startActivity(intent)
//        }
//
//        openChatButton.setOnClickListener() {
//            val intent = Intent(this, ChatListActivity::class.java)
//            startActivity(intent)
//        }
//
//        userProfileButton.setOnClickListener() {
//            val intent = Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        logoutButton.setOnClickListener() {
//            val intent = Intent(this, MainActivity::class.java)
//            mAuth.signOut()
//            startActivity(intent)
//            finish()
//        }
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

