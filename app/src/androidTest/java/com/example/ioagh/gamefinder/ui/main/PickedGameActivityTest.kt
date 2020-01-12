package com.example.ioagh.gamefinder.ui.main

import androidx.test.espresso.intent.Intents
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
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

    private val game = Game()

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
        Thread.sleep(1500)
    }

    @After
    fun unsetData(){
        Intents.release()
        mAuth.signOut()
        Thread.sleep(500)
    }

    @Test
    fun openActivityForGame_seeProperData(){

    }
}