package com.example.ioagh.gamefinder.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.ui.LoginActivity
import org.hamcrest.Matchers.not


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    private lateinit var login: String
    private lateinit var password: String

    @get:Rule
    var mActivityRule: ActivityTestRule<LoginActivity>
            = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun initValidString() {
        login = "login@mail.com"
        password = "password"
    }

    @Test
    fun noInputData_getWarning() {
        onView(withId(R.id.emailLogin))
            .perform(typeText(login), closeSoftKeyboard())

        onView(withId(R.id.loginActivityButton)).perform(click())

        onView(withText(R.string.no_login_data))
            .inRoot(withDecorView
                (not(mActivityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }
}
