package com.example.ioagh.gamefinder

import android.app.Application
import com.google.firebase.FirebaseApp

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}