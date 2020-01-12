package com.example.ioagh.gamefinder.ui.main

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.R

import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.providers.gameTypesReference
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_search_game.*
import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import java.text.SimpleDateFormat

class SearchGameActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    private val PLACE_PICKER_REQUEST = 1;

    private var drawer: DrawerLayout? = null

    private lateinit var mDateSetListener : DatePickerDialog.OnDateSetListener

    private lateinit var mAuth: FirebaseAuth
    private val array : MutableList<String> = mutableListOf()
    private val chosenArray : MutableList<Int> = mutableListOf()
    private var longitude : Double? = null
    private var latitude : Double? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search_game)

        mAuth = FirebaseAuth.getInstance()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView: View = navigationView.inflateHeaderView(R.layout.nav_header)
        headerView.findViewById<TextView>(R.id.user_name_display).text = mAuth.currentUser!!.displayName!!


        initView()
        setNavigationViewListener()
        setDateTimePicker()
        setGameTypes(this)
        searchGame()
    }

    private fun initView() {
        val button = findViewById<Button>(id.searchLocalizationButton)
        button.setOnClickListener {
            val intent = PlacePicker.IntentBuilder()
                .setLatLong(50.049683, 19.944544)
                .showLatLong(true)
                .setMapZoom(12.0f)
                .setAddressRequired(true)
                .hideMarkerShadow(true)
                .setMapType(MapType.NORMAL)
                //   .onlyCoordinates(true)
                .build(this@SearchGameActivity)
            startActivityForResult(intent, PLACE_PICKER_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val addressData = data?.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
                longitude = addressData?.longitude
                latitude = addressData?.latitude
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setGameTypes(ctx: Context){
        gameTypesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    array.add(data.getValue(String::class.java)!!)
                }

                for (value in array) {
                    val checkbox = CheckBox(ctx)
                    checkbox.text = value
                    typeOfGameField.addView(checkbox)
                }
            }
        })
    }

    private fun setDateTimePicker() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            string.open_navigation_drawer, string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mDateSetListener =
            DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
                val month = "0" + (i1 + 1)
                chooseDateField.text = "$i-$month-$i2"
            }

        val spf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        chooseDateField.text = spf.format(Calendar.getInstance().time)

        chooseDateField.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
            )

            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

    }

    private fun searchGame() {

        searchGameButton.setOnClickListener {

            for (i in 0..typeOfGameField.childCount) {
                if (typeOfGameField.getChildAt(i) != null) {
                    val checkbox = typeOfGameField.getChildAt(i) as CheckBox
                    if (checkbox.isChecked) chosenArray.add(i)
                    else if (!checkbox.isChecked && chosenArray.contains(i)){
                        chosenArray.remove(i)
                    }
                }
            }


            val intent = Intent(this, ChooseGameActivity::class.java)
                intent.putExtra("gameName", gameNameField.text.toString())
                intent.putExtra("gameKind", chosenArray.toIntArray())
                intent.putExtra("minNumberOfPeople", minNumberOfPlayersField.text.toString())
                intent.putExtra("maxNumberOfPeople", maxNumberOfPlayersField.text.toString())
                intent.putExtra("range", localizationRangeEditText.text.toString())
                intent.putExtra("longitude", longitude.toString())
                intent.putExtra("latitude", latitude.toString())
//                intent.putExtra("localization", searchLocalizationButton.text.toString())
                intent.putExtra("date", chooseDateField.text.toString())
                intent.putExtra("owner", "")
                startActivity(intent)
        }
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
