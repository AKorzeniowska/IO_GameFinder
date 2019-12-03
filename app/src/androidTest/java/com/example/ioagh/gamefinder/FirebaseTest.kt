package com.example.ioagh.gamefinder

import android.content.Context
import androidx.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.callToast
import com.example.ioagh.gamefinder.providers.createGame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.*

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class FirebaseTest {

    @Mock
    private lateinit var mockContext: Context
    private lateinit var outputString: String

    @Before
    fun setUpContext(){
        mockContext = mock(Context::class.java)
    }

    @Test
    fun saveGameToDatabaseTest(){
        val game = Game("Ryzyko", "Kraków, Prądnik Czerowny",
            Game.OWNER, listOf(1, 2, 3), 5, "2019-12-31 20:00",
            270, "owner_name")
    }
}