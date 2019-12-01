package com.example.ioagh.gamefinder

import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.createGame
import com.google.firebase.FirebaseApp
import org.junit.Test
import java.util.*

class FirebaseUnitTest {

    @Test
    fun addGameToDatabaseTest(){
        val game = Game("Ryzyko", "Kraków, Prądnik Czerowny",
            Game.OWNER, listOf(1, 2, 3), 5, "2019-12-31 20:00", 270)

        createGame(game)
    }
}