package com.example.ioagh.gamefinder.providers

import android.content.Context
import android.content.Intent
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.ui.main.ApplicationActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


private val firebaseDatabase = FirebaseDatabase.getInstance()
private val databaseReference = firebaseDatabase.reference

private val gamesReference = databaseReference.child("games")
private val usersReference = databaseReference.child("users")
private val gameTypesReference = databaseReference.child("game_types")

fun createGame(game: Game, context: Context){
    val pushedGameReference: DatabaseReference = gamesReference.push()
    pushedGameReference.setValue(game)
        .addOnCompleteListener(OnCompleteListener<Void>() {
            @Override
            fun onComplete(@NonNull task: Task<String>) {
                if (task.isSuccessful) {
                    Toast.makeText(context, "Rozgrywka dodana!", Toast.LENGTH_SHORT)
                }
            }
        })
}

fun createUser(username: String?){
    username?.let { usersReference.child(it).setValue(0) }
}

fun userExists(username: String): Boolean{
    var result: Boolean = false
    usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            result = true
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.hasChild(username)) {
                result = true
            }
        }
    })
    return result
}