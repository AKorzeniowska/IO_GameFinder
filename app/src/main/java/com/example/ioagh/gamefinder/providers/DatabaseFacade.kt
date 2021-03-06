package com.example.ioagh.gamefinder.providers

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*


val firebaseDatabase = FirebaseDatabase.getInstance()
val databaseReference = firebaseDatabase.reference

val gamesReference = databaseReference.child("games")
val usersReference = databaseReference.child("users")
val gameTypesReference = databaseReference.child("game_types")

fun createGame(game: Game): String?{
    val pushedGameReference: DatabaseReference = gamesReference.push()
    pushedGameReference.setValue(game)
    return pushedGameReference.key
}

fun createUser(user: User, username: String){
    usersReference.child(username).setValue(user)
}


fun decreaseGamersNumber(gameId: String){
    gamesReference.child(gameId).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val game = dataSnapshot.getValue<Game>(Game::class.java)
            gamesReference.child(gameId).child("players").setValue(game!!.players!! - 1)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
        }
    })
}

fun increaseGamersNumber(gameId: String){
    gamesReference.child(gameId).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val game = dataSnapshot.getValue<Game>(Game::class.java)
            gamesReference.child(gameId).child("players").setValue(game!!.players!! + 1)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
        }
    })
}

fun addGameToJoined(gameId: String, username: String){
    usersReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue<User>(User::class.java)
            val gameList = user?.joinedGames as ArrayList<String>
            if (!gameList.contains(gameId)) {
                gameList.add(gameId)
            }
            usersReference.child(username).setValue(user)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
        }
    })
}

fun addGameToOwned(gameId: String, username: String){
    usersReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue<User>(User::class.java)
            val gameList = user?.createdGames as ArrayList<String>
            if (!gameList.contains(gameId)) {
                gameList.add(gameId)
            }
            usersReference.child(username).setValue(user)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
        }
    })
}

fun setViewIfUserJoinedGame(gameId: String, username: String, button: Button){
    usersReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue<User>(User::class.java)
            val gameListFuture = user?.joinedGames as ArrayList<String>
            val gameListOwned = user.createdGames as ArrayList<String>
                    if (gameListFuture.contains(gameId)) {
                        button.isClickable = false
                        button.text = "Już dołączyłeś"
                    }
                    if (gameListOwned.contains(gameId)){
                        button.isClickable = false
                        button.text = "To Twoja gra"
                    }
                setViewIfGameIsFull(gameId, button)
                }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
        }
    })
}

fun setViewIfGameIsFull(gameId: String, button: Button){
    gamesReference.child(gameId).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            val game = p0.getValue(Game::class.java)
            if (game!!.maxPlayers == game.players){
                button.text = "Brak miejsc"
                button.visibility = View.VISIBLE
            }
        }
    })
}


fun callToast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}