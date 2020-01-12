package com.example.ioagh.gamefinder.ui.main

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.addGameToOwned
import com.example.ioagh.gamefinder.providers.gameTypesReference
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.providers.parseStringToMinutes
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_game.*
import kotlinx.android.synthetic.main.activity_search_game.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.R.string.*
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.ioagh.gamefinder.MainActivity
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.android.material.navigation.NavigationView

class AddGameActivity : NavigationView.OnNavigationItemSelectedListener, AppCompatActivity() {

    private var drawer: DrawerLayout? = null

    private val array : MutableList<String> = mutableListOf()
    private lateinit var mAuth: FirebaseAuth

    private lateinit var mDateSetListener : DatePickerDialog.OnDateSetListener
    private lateinit var mTimeSetListener : TimePickerDialog.OnTimeSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_game)

        val toolbar = toolbar_add_game
        setSupportActionBar(toolbar)

        drawer = drawer_layout_add_game

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            string.open_navigation_drawer, string.close_navigation_drawer
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        mAuth = FirebaseAuth.getInstance()
        val navigationView = findViewById<NavigationView>(com.example.ioagh.gamefinder.R.id.nav_view)
        val headerView: View = navigationView.inflateHeaderView(com.example.ioagh.gamefinder.R.layout.nav_header)
        headerView.findViewById<TextView>(com.example.ioagh.gamefinder.R.id.user_name_display).text = mAuth.currentUser!!.displayName!!

        setNavigationViewListener()
        initView()
    }

    private fun initView() {

        setGameTypes(this)
        setDateTimePicker()

        addGameButton.setOnClickListener() {
            if (!(gameNameEditText.text.isNullOrBlank() ||
                numberOfPlayersEditText.text.isNullOrBlank() ||
                searchLocalizationEditText.text.isNullOrBlank() ||
                gameTimeEditText.text.isNullOrBlank() ||
                gameTypeRadioGroup.checkedRadioButtonId == -1)) {
                val game = buildGame()
                createGame(game)
            } else {
                Toast.makeText(this, "Uzupełnij wszystkie pola!", Toast.LENGTH_LONG).show()
            }
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
                        gameKindCheckBoxes.addView(checkbox)
                    }
                }
            })
    }

    private fun buildGame(): Game {
        val game = Game()

        //game.date = addDateTextView.text.toString() + " " + addTimeTextView.text.toString()
        game.durationInMinutes = parseStringToMinutes(gameTimeEditText.text.toString())
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
        game.players = numberOfPlayersEditText.text.toString().toInt()
        game.gameName = gameNameEditText.text.toString()
        game.maxPlayers = numberOfPlayersEditText.text.toString().toInt()
        game.date = gameDatePicker.text.toString()

        if (mAuth.currentUser != null && mAuth.currentUser!!.displayName != null) {
            game.owner = mAuth.currentUser!!.displayName
        }
        return game
    }

    private fun createGame(game: Game){
        val pushedGameReference: DatabaseReference = gamesReference.push()
        pushedGameReference.setValue(game)
            .addOnSuccessListener {
                addGameToOwned(pushedGameReference.key.toString(), mAuth.currentUser!!.displayName!!)
                Toast.makeText(this, "Rozgrywka dodana!", Toast.LENGTH_SHORT).show()
                finish()
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
            com.example.ioagh.gamefinder.R.id.nav_chat -> {
                intent = Intent(this, ChatListActivity::class.java)
                startActivity(intent)
            }
            com.example.ioagh.gamefinder.R.id.nav_search_game -> {
                intent = Intent(this, SearchGameActivity::class.java)
                startActivity(intent)
            }
            com.example.ioagh.gamefinder.R.id.nav_add_game -> {
                intent = Intent(this, AddGameActivity::class.java)
                startActivity(intent)
            }
            com.example.ioagh.gamefinder.R.id.nav_profile -> {
                intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            com.example.ioagh.gamefinder.R.id.nav_logout -> {
                mAuth.signOut()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    private fun setNavigationViewListener() {
        val navigationView = findViewById<NavigationView>(com.example.ioagh.gamefinder.R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setDateTimePicker(){
        mDateSetListener =
            DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
                val month = "0" + (i1 + 1)
                gameDatePicker.text = "$i-$month-$i2"
            }

        val spf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        gameDatePicker.text = spf.format(Calendar.getInstance().time)

        gameDatePicker.setOnClickListener() {
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
}
