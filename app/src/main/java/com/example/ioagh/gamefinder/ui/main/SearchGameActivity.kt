package com.example.ioagh.gamefinder.ui.main

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_search_game.*
import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_game.*
import java.text.SimpleDateFormat

class SearchGameActivity : AppCompatActivity() {

    private lateinit var mDateSetListener : DatePickerDialog.OnDateSetListener

    private var list: ArrayList<Game> = ArrayList<Game>()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_search_game)
        mDateSetListener = DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            chooseDateField.text  = "$i-$i1-$i2"
        }

        var spf = SimpleDateFormat ("yyyy-MM-dd", Locale.US)
        chooseDateField.text = spf.format(Calendar.getInstance().time)

        chooseDateField.setOnClickListener() {
            val calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                mDateSetListener,
                year, month, day)

            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        searchGame()
    }

    fun searchGame() {
        searchGameButton.setOnClickListener {
                val intent = Intent(this, ChooseGameActivity::class.java)
                intent.putExtra("gameName", gameNameField.text.toString())
                intent.putExtra("gameKind", typeOfGameField.text.toString())
                intent.putExtra("minNumberOfPeople", minNumberOfPlayersField.text.toString())
                intent.putExtra("maxNumberOfPeople", maxNumberOfPlayersField.text.toString())
                intent.putExtra("localization", searchLocalizationEdit.text.toString())
                intent.putExtra("date", chooseDateField.text.toString())
                startActivity(intent)
        }
    }
}
