package com.example.ioagh.gamefinder.ui.main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.R

import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_search_game.*
import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_game.*
import java.text.SimpleDateFormat

class SearchGameActivity : AppCompatActivity() {

    private var drawer: DrawerLayout? = null

    private lateinit var mDateSetListener : DatePickerDialog.OnDateSetListener
    private lateinit var mTimeSetListener : TimePickerDialog.OnTimeSetListener

    private lateinit var mAuth: FirebaseAuth

    private var list: ArrayList<Game> = ArrayList<Game>()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search_game)

        mAuth = FirebaseAuth.getInstance()
        setDateTimePicker()
        searchGame()
    }

    private fun setDateTimePicker(){

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            string.open_navigation_drawer, string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mDateSetListener = DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            chooseDateField.text  = "$i-$i1-$i2"
        }
//        mTimeSetListener = TimePickerDialog.OnTimeSetListener{
//                view: TimePicker?, hourOfDay: Int, minute: Int -> chooseTimeField.text =
//            "$hourOfDay:$minute"
//        }

        val spf = SimpleDateFormat ("yyyy-MM-dd", Locale.US)
        chooseDateField.text = spf.format(Calendar.getInstance().time)

        chooseDateField.setOnClickListener() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                mDateSetListener,
                year, month, day)

            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        val stf = SimpleDateFormat("hh:mm", Locale.US)
//        chooseTimeField.text = stf.format(Calendar.getInstance().time)
//
//        chooseTimeField.setOnClickListener{
//            val calendar = Calendar.getInstance()
//            val hour = calendar.get(Calendar.HOUR_OF_DAY)
//            val minute = calendar.get(Calendar.MINUTE)
//
//            val timeDialog = TimePickerDialog(this,
//                android.R.style.Theme_Holo_Dialog_MinWidth,
//                mTimeSetListener,
//                hour, minute, true)
//
//            timeDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            timeDialog.show()
//        }
    }

    private fun searchGame() {
        searchGameButton.setOnClickListener {
                val intent = Intent(this, ChooseGameActivity::class.java)
                intent.putExtra("gameName", gameNameField.text.toString())
                intent.putExtra("gameKind", typeOfGameField.text.toString())
                intent.putExtra("minNumberOfPeople", minNumberOfPlayersField.text.toString())
                intent.putExtra("maxNumberOfPeople", maxNumberOfPlayersField.text.toString())
                intent.putExtra("localization", searchLocalizationEdit.text.toString())
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
}
