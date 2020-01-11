package com.example.ioagh.gamefinder

import android.content.Context
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.providers.createGame
import com.example.ioagh.gamefinder.providers.firebaseDatabase
import com.example.ioagh.gamefinder.providers.gamesReference
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.util.*


class FirebaseUnitTest {

    private val email = "testtest@test.com"
    private val password = "haslo123"
    private val gameName = "Game Example"
    private val localization = "Kraków, Prądnik Czerowny"
    private val owner = "owner"
    private var key : String? = ""
    val ctx = mock(Context::class.java)

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(ctx)
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        mAuth.signInWithEmailAndPassword(email, password)
    }

    @Test
    fun addGameToDatabaseTest(){
        //given
        val game = Game(gameName, localization,
            Game.OWNER, listOf(1, 2, 3), 5, 9, "2019-12-31 20:00", 270, owner)

        //when
        key = createGame(game)
        Thread.sleep(2000)

        //then
        gamesReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }
            override fun onDataChange(p0: DataSnapshot) {
                var isTrue = false
                for (child in p0.children) {
                    if (child.key == key) {
                        isTrue = true
                        break
                    }
                }
                assertTrue(isTrue)
            }
        })
    }

    @After
    fun removeUserFromDatabase() {
        gamesReference.orderByKey().equalTo(key).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.ref.removeValue()
            }

        })
    }
}