package com.example.ioagh.gamefinder

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.createGame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseTest {
    @Test
    fun saveGameToDatabaseTest(){
        val game = Game("Ryzyko", "Kraków, Prądnik Czerowny",
            Game.OWNER, listOf(1, 2, 3), 5, "2019-12-31 20:00", 270)

//        val context: Context = mock(Context::class.java)

//        val id = createGame(game, )
    }
}