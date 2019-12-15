package com.example.ioagh.gamefinder.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.User
import com.example.ioagh.gamefinder.providers.usersReference
import com.example.ioagh.gamefinder.ui.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityCreateUserTest {
    private val nick = "test2"
    private val properMail = "test23@test.com"
    private val password = "password"
    private val name = "name"
    private val age = "20"

    @get:Rule
    var mActivityRule: ActivityTestRule<RegisterActivity>
            = ActivityTestRule(RegisterActivity::class.java)

    @Before
    fun setData(){
        Intents.init()
    }

    @After
    fun unsetData(){
        Intents.release()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.delete()
        usersReference.child(nick).removeValue()
        Thread.sleep(3000)
    }

    @Test
    fun signUp_userGetsCreated(){
        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.nickRegister))
            .perform(typeText(nick), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.typeText(properMail), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        Thread.sleep(6000)

        usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                assertEquals(true, snapshot.hasChild(nick))
            }
        })

        Thread.sleep(2000)
    }

    @Test
    fun signUpWithAgeAndName_userGetsCreated(){
        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.nickRegister))
            .perform(typeText(nick), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.typeText(properMail), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.nameRegister))
            .perform(ViewActions.typeText(name), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.ageRegister))
            .perform(ViewActions.typeText(age), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        Thread.sleep(7500)

        usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                assertEquals(true, snapshot.hasChild(nick))
                val user = snapshot.child(nick).getValue(User::class.java)
                assertEquals(name, user?.name)
                assertEquals(age.toInt(), user?.age)
            }
        })

        Thread.sleep(2000)
    }
}