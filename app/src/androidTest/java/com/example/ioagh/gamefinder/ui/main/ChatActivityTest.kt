package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.charset.Charset
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4::class)
class ChatActivityTest {
    private val email = "testtest@test.com"
    private val password = "haslo123"
    private val email2 = "testtest2@test.com"

    private val firstMessage = "hello"
    private val secondMessage = "hi"

    private lateinit var mAuth: FirebaseAuth


    @get:Rule
    var mActivityRule: ActivityTestRule<ChatActivity>
            = ActivityTestRule(ChatActivity::class.java, true, false)

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
    fun sendMessage_messageIsVisibleForOthers(){
        val intent = Intent()
        intent.putExtra("chatroom", getRandomString())
        mActivityRule.launchActivity(intent)
        Thread.sleep(1500)

        onView(withId(R.id.editText)).perform(replaceText(firstMessage), closeSoftKeyboard())
        onView(withId(R.id.chatSendButton)).perform(click())
        Thread.sleep(500)

        mActivityRule.finishActivity()
        mAuth.signOut()
        mAuth.signInWithEmailAndPassword(email2, password)
        Thread.sleep(1500)

        mActivityRule.launchActivity(intent)
        Thread.sleep(1500)

        onView(withText(firstMessage)).check(matches(isDisplayed()))

        onView(withId(R.id.editText)).perform(replaceText(secondMessage), closeSoftKeyboard())
        onView(withId(R.id.chatSendButton)).perform(click())
        Thread.sleep(500)

        mActivityRule.finishActivity()
        mAuth.signOut()
        mAuth.signInWithEmailAndPassword(email, password)
        Thread.sleep(1500)

        mActivityRule.launchActivity(intent)
        Thread.sleep(1500)

        onView(withText(firstMessage)).check(matches(isDisplayed()))
        onView(withText(secondMessage)).check(matches(isDisplayed()))
    }

    private fun getRandomString(): String{
        val array = ByteArray(7)

        Random().nextBytes(array)
        return String(array, Charset.forName("UTF-8"))
    }
}