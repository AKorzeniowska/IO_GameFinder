package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.providers.parseStringToMinutes
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import junit.framework.Assert.assertEquals
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddGameActivityCreateGameTest {

    private lateinit var mAuth: FirebaseAuth
    private val name = "Test Name"
    private val maxPlayers = 5
    private val duration = "2:30"

    private val email = "testtest@test.com"
    private val password = "haslo123"

    private lateinit var gameHash: String


    @get:Rule
    var mActivityRule: ActivityTestRule<AddGameActivity>
            = ActivityTestRule(AddGameActivity::class.java, true, false)

    @Before
    fun setData(){
        Intents.init()
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        mAuth.signInWithEmailAndPassword(email, password)
        println(mAuth.currentUser)
        Thread.sleep(3000)
    }

    @After
    fun unsetData(){
        Intents.release()
        usersReference.child(mAuth.currentUser!!.displayName!!).child("createdGames").removeValue()
        gamesReference.child(gameHash).removeValue()
        mAuth.signOut()
        Thread.sleep(2000)
    }

    @Test
    fun inputProperData_gameGetsCreated(){
        mActivityRule.launchActivity(Intent())
        onView(withId(R.id.gameNameEditText)).perform(scrollTo(), typeText(name), closeSoftKeyboard())
        onView(withId(R.id.gameTimeEditText)).perform(scrollTo(), typeText(duration), closeSoftKeyboard())
        onView(withText("Użyj obecnej lokalizacji")).perform(scrollTo(), click())
        onView(withId(R.id.numberOfPlayersEditText)).perform(scrollTo(), typeText(maxPlayers.toString()), closeSoftKeyboard())
        onView(withId(R.id.gameOwnerRadioButton)).perform(scrollTo(), click())
        onView(withText("przygodowe")).perform(scrollTo(), click())

        onView(withId(R.id.addGameButton)).perform(scrollTo(), click())

        Thread.sleep(3000)
//        onView(withText(R.string.game_added))
//            .inRoot(withDecorView
//                (not(mActivityRule.activity.window.decorView)))
//            .check(matches(isDisplayed()))

        usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                assert(snapshot.hasChild(mAuth.currentUser!!.displayName!!))
                val user = snapshot.child(mAuth.currentUser!!.displayName!!).getValue(User::class.java)
                assert(user!!.createdGames != null)
                assert(user.createdGames!!.size == 1)

                val game = user.createdGames!![0]
                gameHash = game
                gamesReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        assert(snapshot.hasChild(game))
                        val gameObject = snapshot.child(game).getValue(Game::class.java)
                        assertEquals(name, gameObject!!.gameName)
                        assertEquals(maxPlayers, gameObject.maxPlayers)
                        assertEquals(0, gameObject.players)
                        assertEquals(mAuth.currentUser!!.displayName!!, gameObject.owner)
                        assertEquals(parseStringToMinutes(duration), gameObject.durationInMinutes)
                        assert(gameObject.longitude != null)
                        assert(gameObject.latitude != null)
                        assert(gameObject.gameTypes != null)
                        assert(gameObject.gameTypes!!.isNotEmpty())
                    }
                })
            }
        })
        Thread.sleep(5000)
    }
}