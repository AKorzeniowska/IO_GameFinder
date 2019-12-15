package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.calendar
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.providers.parseStringToMinutes
import com.example.ioagh.gamefinder.providers.usersReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_add_game.*
import kotlinx.android.synthetic.main.activity_login.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddGameActivityTest {

    private lateinit var mAuth: FirebaseAuth
    private val name = "Test Name"
    private val maxPlayers = 5
    private val localization = "Test locale"
    private val duration = "2:30"

    private val email = "test@test.com"
    private val password = "password"


    @get:Rule
    var mActivityRule: ActivityTestRule<AddGameActivity>
            = ActivityTestRule(AddGameActivity::class.java)

    @Before
    fun setData(){
        Intents.init()
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        mAuth.signInWithEmailAndPassword(email, password)
        Thread.sleep(1500)
    }

    @After
    fun unsetData(){
        Intents.release()
        mAuth.signOut()
        Thread.sleep(500)
    }

    @Test
    fun openActivity_seeCurrentDate(){
        val spf = SimpleDateFormat ("yyyy-MM-dd", Locale.US)
        val stf = SimpleDateFormat ("hh:mm", Locale.GERMANY)
        onView(withId(R.id.addDateTextView)).check(matches(withText(spf.format(Calendar.getInstance().time))))
        onView(withId(R.id.addTimeTextView)).check(matches(withText(stf.format(Calendar.getInstance().time))))
    }

    @Test
    fun noInputData_getWarning(){
        onView(withId(R.id.addGameButton)).perform(scrollTo(), click())

        onView(withText(R.string.invalid_game_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.gameNameEditText)).perform(scrollTo(), typeText(name), closeSoftKeyboard())
        onView(withId(R.id.addGameButton)).perform(scrollTo(), click())

        onView(withText(R.string.invalid_game_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.gameTimeEditText)).perform(scrollTo(), typeText(duration), closeSoftKeyboard())
        onView(withId(R.id.addGameButton)).perform(scrollTo(), click())

        onView(withText(R.string.invalid_game_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.numberOfPlayersEditText)).perform(scrollTo(), typeText(maxPlayers.toString()), closeSoftKeyboard())
        onView(withId(R.id.addGameButton)).perform(scrollTo(), click())

        onView(withText(R.string.invalid_game_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.searchLocalizationEditText)).perform(scrollTo(), typeText(localization), closeSoftKeyboard())
        onView(withId(R.id.addGameButton)).perform(scrollTo(), click())

        onView(withText(R.string.invalid_game_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }


}