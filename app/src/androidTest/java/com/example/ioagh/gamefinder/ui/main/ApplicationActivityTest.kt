package com.example.ioagh.gamefinder.ui.main

import android.app.Instrumentation
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId

import androidx.test.runner.AndroidJUnit4
import kotlinx.android.synthetic.main.activity_application.*
import org.junit.Assert.*
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.ui.profile.ProfileActivity
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ApplicationActivityTest {

    @Test
    fun testButtons() {

   //     Instrumentation.ActivityMonitor activityMonitor = new Instrumentation().addMonitor(ProfileActivity::class.java, null, false)

        onView(withId(id.userProfileButton)).perform(ViewActions.click())
    }
}