package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.profile.EditProfileActivity
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {
    private val email = "testtest@test.com"
    private val password = "haslo123"

    private lateinit var mAuth: FirebaseAuth

    @get:Rule
    var mActivityRule: ActivityTestRule<ProfileActivity>
            = ActivityTestRule(ProfileActivity::class.java, true, false)

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
    fun openProfile_seeUsersData(){
        val intent = Intent()
        mActivityRule.launchActivity(intent)
        onView(withId(R.id.nick)).check(matches(withText(mAuth.currentUser!!.displayName!!)))
        onView(withId(R.id.email)).check(matches(withText(mAuth.currentUser!!.email!!)))
        Thread.sleep(500)
        onView(withId(R.id.name)).check(matches(withText("test")))
        onView(withId(R.id.age)).check(matches(withText("20")))
    }

    @Test
    fun clickProfileEdit_openEditActivity(){
        val intent = Intent()
        mActivityRule.launchActivity(intent)
        onView(withId(R.id.edit_image_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(EditProfileActivity::class.java.name))
    }
}