package com.example.ioagh.gamefinder.ui.main

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.withDecorView
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
class LoginActivityTest {

    private lateinit var login: String
    private lateinit var password: String
    private lateinit var mail: String

    @get:Rule
    var mActivityRule: ActivityTestRule<LoginActivity>
            = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setData(){
        Intents.init()
        login = "login"
        password = "password"
        mail = "test@test.com"
    }

    @After
    fun unsetData(){
        Intents.release()
    }

    @Test
    fun noInputData_getWarning() {
        onView(withId(R.id.loginActivityButton)).perform(click())

        onView(withText(R.string.no_login_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.emailLogin))
            .perform(typeText(login), closeSoftKeyboard())

        onView(withId(R.id.loginActivityButton)).perform(click())

        onView(withText(R.string.no_login_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.emailLogin))
            .perform(replaceText(""), closeSoftKeyboard())

        onView(withId(R.id.passwordLogin))
            .perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.loginActivityButton)).perform(click())

        onView(withText(R.string.no_login_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun inputInvalidData_getWarning() {
        onView(withId(R.id.emailLogin))
            .perform(typeText(login), closeSoftKeyboard())

        onView(withId(R.id.passwordLogin))
            .perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.loginActivityButton)).perform(click())

        onView(withText(R.string.invalid_login_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun inputValidData_LogIn(){
        onView(withId(R.id.emailLogin))
            .perform(typeText(mail), closeSoftKeyboard())

        onView(withId(R.id.passwordLogin))
            .perform(typeText(password), closeSoftKeyboard())

        onView(withId(R.id.loginActivityButton)).perform(click())

        Thread.sleep(2500)
        onView(withText(R.string.login_successful))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))

        intended(hasComponent(ApplicationActivity::class.java.name))
    }

    @Test
    fun clickRegisterButton_OpenRegisterActivity(){
        onView(withId(R.id.registerInsteadOfLoginButton))
            .perform(click())

        Thread.sleep(1000)
        intended(hasComponent(RegisterActivity::class.java.name))
    }
}
