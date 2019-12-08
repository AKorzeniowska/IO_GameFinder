package com.example.ioagh.gamefinder.ui.main

import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_game.*

class AddGameActivity : AppCompatActivity() {

    private val array : Array<String> = arrayOf("1", "2" , "3")
    //TODO replace with actual gameKinds
    private lateinit var mAuth: FirebaseAuth
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_game)

        mAuth = FirebaseAuth.getInstance()
        initView()
    }

    private fun initView() {
        for (value in array) {
            val checkbox = CheckBox(this)
            checkbox.setText(value)
            gameKindCheckBoxes.addView(checkbox)
        }

        addGameButton.setOnClickListener() {
            if (!(gameNameEditText.text.isNullOrBlank() ||
                numberOfPlayersEditText.text.isNullOrBlank() ||
                searchLocalizationEditText.text.isNullOrBlank() ||
                gameTimeEditText.text.isNullOrBlank() || gameTypeRadioGroup.checkedRadioButtonId == -1)) {
                val game = createGame()
                databaseReference.child("games").push().setValue(game)
            } else {
                Toast.makeText(this, "Jakies pole puste", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createGame() :Game {
        val game = Game()

        game.date = gameDatePicker.year.toString().plus("-").plus(gameDatePicker.month.toString()).plus("-")
            .plus(gameDatePicker.dayOfMonth.toString())
        game.durationInMinutes = gameTimeEditText.text.toString().toInt()
        val list = ArrayList<Int>()
        for (i in 0..gameKindCheckBoxes.childCount) {
            if (gameKindCheckBoxes.getChildAt(i) != null) {
                val checkbox = gameKindCheckBoxes.getChildAt(i) as CheckBox
                if (checkbox.isChecked) list.add(i)
            }
        }
        game.gameTypes = list
        game.gameKind = gameTypeRadioGroup.checkedRadioButtonId
        game.localization = searchLocalizationEditText.text.toString()
        game.gamers = numberOfPlayersEditText.text.toString().toInt()
        game.gameName = gameNameEditText.text.toString()

        if (mAuth.currentUser != null && mAuth.currentUser!!.displayName != null) {
            game.owner = mAuth.currentUser!!.displayName
        }
        return game
    }
}
