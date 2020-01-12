package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.profile.EditProfileActivity
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class EditProfileActivityTest {
    private val email = "testtest@test.com"
    private val password = "haslo123"
    private val properName = "test"
    private val properAge = "20"

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
        usersReference.child(mAuth.currentUser!!.displayName!!).child("name").setValue(properName)
        usersReference.child(mAuth.currentUser!!.displayName!!).child("age").setValue(properAge.toInt())
        Thread.sleep(500)

        Intents.release()
        mAuth.signOut()
        Thread.sleep(500)
    }

    @Test
    fun openEditProfile_seeProperData(){
        mActivityRule.launchActivity(Intent())
        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        onView(withId(R.id.nameEdit)).check(matches(withText(properName)))
        onView(withId(R.id.ageEdit)).check(matches(withText(properAge)))
    }

    @Test
    fun inputData_saveChanges(){
        val newname = "new_name"
        val newage = "45"

        mActivityRule.launchActivity(Intent())
        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        onView(withId(R.id.nameEdit)).perform(replaceText(newname), closeSoftKeyboard())
        onView(withId(R.id.ageEdit)).perform(replaceText(newage), closeSoftKeyboard())

        onView(withId(R.id.acceptChanges)).perform(click())
        Thread.sleep(500)
        onView(withText("Pomyślnie zapisano zmiany"))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        Espresso.pressBack()
        Thread.sleep(500)
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))
        onView(withId(R.id.name)).check(matches(withText(newname)))
        onView(withId(R.id.age)).check(matches(withText(newage)))

        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        onView(withId(R.id.nameEdit)).check(matches(withText(newname)))
        onView(withId(R.id.ageEdit)).check(matches(withText(newage)))
    }

    @Test
    fun inputEmptyData_doNotSaveChanges(){
        val newname = ""
        val newage = ""

        mActivityRule.launchActivity(Intent())
        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        onView(withId(R.id.nameEdit)).perform(replaceText(newname), closeSoftKeyboard())
        onView(withId(R.id.ageEdit)).perform(replaceText(newage), closeSoftKeyboard())

        onView(withId(R.id.acceptChanges)).perform(click())
        Thread.sleep(500)
        onView(withText("Pomyślnie zapisano zmiany"))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(doesNotExist())

        Espresso.pressBack()
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))

        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        onView(withId(R.id.nameEdit)).check(matches(withText(properName)))
        onView(withId(R.id.ageEdit)).check(matches(withText(properAge)))
    }

    @Test
    fun leaveDataUnchanged_doNotSaveChanges(){
        mActivityRule.launchActivity(Intent())
        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.acceptChanges)).perform(click())
        Thread.sleep(500)
        onView(withText("Pomyślnie zapisano zmiany"))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(doesNotExist())

        Espresso.pressBack()
        Intents.intended(IntentMatchers.hasComponent(ProfileActivity::class.java.name))

        onView(withId(R.id.edit_image_button)).perform(click())
        Thread.sleep(1500)

        onView(withId(R.id.nameEdit)).check(matches(withText(properName)))
        onView(withId(R.id.ageEdit)).check(matches(withText(properAge)))
    }
}