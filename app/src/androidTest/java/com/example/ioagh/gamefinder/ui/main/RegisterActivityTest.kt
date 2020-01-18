package com.example.ioagh.gamefinder.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.ui.LoginActivity
import com.example.ioagh.gamefinder.ui.RegisterActivity
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityTest {

    private val nick = "test"
    private val mail = "test"
    private val properMail = "test23@test.com"
    private val password = "password"
    private val secondPassword = "another_password"

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
    }

    @Test
    fun inputTwoDifferentPasswords_getWarning(){
        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.typeText(secondPassword), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        onView(withText(R.string.two_different_passwords))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun noInputData_getWarning(){
        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        onView(withText(R.string.no_registration_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.typeText(mail), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        onView(withText(R.string.no_registration_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.nickRegister))
            .perform(typeText(nick), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        onView(withText(R.string.no_registration_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun passwordTooShort_getWarning(){
        onView(withId(R.id.nickRegister))
            .perform(typeText(nick), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.typeText(mail), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.replaceText("x"), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.replaceText("x"), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        onView(withText(R.string.password_too_short))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun invalidEmail_getWarning(){
        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.nickRegister))
            .perform(typeText(nick), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.typeText(mail), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        onView(withText(R.string.invalid_email_address))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun inputRepeatedNick_getWarning(){
        onView(ViewMatchers.withId(R.id.passwordRegister))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.passwordRegisterRepeat))
            .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.nickRegister))
            .perform(typeText(nick), ViewActions.closeSoftKeyboard())

        onView(ViewMatchers.withId(R.id.emailRegister))
            .perform(ViewActions.typeText(properMail), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.registerActivityButton)).perform(click())

        Thread.sleep(1000)
        onView(withText(R.string.user_already_exists))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }
}