package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.gamesReference
import com.example.ioagh.gamefinder.providers.parseMinutesToSring
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_picked_game.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PickedGameActivityTest {
    private val email = "testtest@test.com"
    private val password = "haslo123"
    private val gameHash = "test"

    private val game = Game("testname", 50.06280051206192, 50.06280051206192, 0, listOf(2, 1), 4, 6, "2020-01-17", 360, "test")

    private lateinit var mAuth: FirebaseAuth

    @get:Rule
    var mActivityRule: ActivityTestRule<PickedGameActivity>
            = ActivityTestRule(PickedGameActivity::class.java, true, false)

    @Before
    fun setData(){
        Intents.init()
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        mAuth.signInWithEmailAndPassword(email, password)
        gamesReference.child(gameHash).setValue(game)
        Thread.sleep(1500)
    }

    @After
    fun unsetData(){
        Intents.release()
        gamesReference.child(gameHash).removeValue()
        usersReference.child(mAuth.currentUser!!.displayName!!).child("joinedGames").removeValue()
        usersReference.child(mAuth.currentUser!!.displayName!!).child("createdGames").removeValue()
        Thread.sleep(500)
        mAuth.signOut()
        Thread.sleep(500)
    }

    @Test
    fun openActivityForGame_seeProperData(){
        val intent = Intent()
        intent.putExtra("gameHash", gameHash)
        mActivityRule.launchActivity(intent)
        Thread.sleep(1000)

        onView(withId(R.id.gameName)).check(matches(withText(game.gameName)))
        onView(withId(R.id.predictedGameTime)).check(matches(withText(parseMinutesToSring(game.durationInMinutes!!))))
        onView(withId(R.id.currentNumberOfPlayers)).check(matches(withText(game.players.toString())))
        onView(withId(R.id.maximumNumberOfPlayers)).check(matches(withText(game.maxPlayers.toString())))

        onView(withId(R.id.joinGameButton)).check(matches(withText(R.string.join_game)))
    }

    @Test
    fun openActivityForGameWithMaxPlayers_seeProperButton(){
        gamesReference.child(gameHash).child("players").setValue(game.maxPlayers)
        val intent = Intent()
        intent.putExtra("gameHash", gameHash)
        mActivityRule.launchActivity(intent)
        Thread.sleep(1000)

        onView(withId(R.id.gameName)).check(matches(withText(game.gameName)))
        onView(withId(R.id.predictedGameTime)).check(matches(withText(parseMinutesToSring(game.durationInMinutes!!))))
        onView(withId(R.id.currentNumberOfPlayers)).check(matches(withText(game.maxPlayers.toString())))
        onView(withId(R.id.maximumNumberOfPlayers)).check(matches(withText(game.maxPlayers.toString())))

        onView(withId(R.id.joinGameButton)).check(matches(withText(R.string.no_slots_left)))
    }


    @Test
    fun openActivityForGameThatYouJoined_seeProperButton(){
        usersReference.child(mAuth.currentUser!!.displayName!!).child("joinedGames").child("0").setValue(gameHash)
        val intent = Intent()
        intent.putExtra("gameHash", gameHash)
        mActivityRule.launchActivity(intent)
        Thread.sleep(1000)

        onView(withId(R.id.gameName)).check(matches(withText(game.gameName)))
        onView(withId(R.id.predictedGameTime)).check(matches(withText(parseMinutesToSring(game.durationInMinutes!!))))
        onView(withId(R.id.currentNumberOfPlayers)).check(matches(withText(game.players.toString())))
        onView(withId(R.id.maximumNumberOfPlayers)).check(matches(withText(game.maxPlayers.toString())))

        onView(withId(R.id.joinGameButton)).check(matches(withText(R.string.already_joined)))
    }

    @Test
    fun openActivityForGameThatYouOwn_seeProperButton(){
        usersReference.child(mAuth.currentUser!!.displayName!!).child("createdGames").child("0").setValue(gameHash)
        val intent = Intent()
        intent.putExtra("gameHash", gameHash)
        mActivityRule.launchActivity(intent)
        Thread.sleep(1500)

        onView(withId(R.id.gameName)).check(matches(withText(game.gameName)))
        onView(withId(R.id.predictedGameTime)).check(matches(withText(parseMinutesToSring(game.durationInMinutes!!))))
        onView(withId(R.id.currentNumberOfPlayers)).check(matches(withText(game.players.toString())))
        onView(withId(R.id.maximumNumberOfPlayers)).check(matches(withText(game.maxPlayers.toString())))

        onView(withId(R.id.joinGameButton)).check(matches(withText(R.string.its_your_game)))
    }

    @Test
    fun joinGame_numberOfPlayersIncreased(){
        val intent = Intent()
        intent.putExtra("gameHash", gameHash)
        mActivityRule.launchActivity(intent)
        Thread.sleep(1500)

        onView(withId(R.id.joinGameButton)).check(matches(withText(R.string.join_game))).perform(click())
        Espresso.pressBack()
        mActivityRule.finishActivity()

        mActivityRule.launchActivity(intent)
        Thread.sleep(1000)
        onView(withId(R.id.gameName)).check(matches(withText(game.gameName)))
        onView(withId(R.id.predictedGameTime)).check(matches(withText(parseMinutesToSring(game.durationInMinutes!!))))
        onView(withId(R.id.currentNumberOfPlayers)).check(matches(withText((game.players!! + 1).toString())))
        onView(withId(R.id.maximumNumberOfPlayers)).check(matches(withText(game.maxPlayers.toString())))
        onView(withId(R.id.joinGameButton)).check(matches(withText(R.string.already_joined)))
    }

}