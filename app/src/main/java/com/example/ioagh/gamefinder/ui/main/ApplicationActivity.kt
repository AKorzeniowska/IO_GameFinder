package com.example.ioagh.gamefinder.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ioagh.gamefinder.R.*
class ApplicationActivity : AppCompatActivity() {

    companion object DatabaseInfoProvider {
        const val JDBC_DRIVER = ""
        const val URL = "sql7.freesqldatabase.com"
        const val USER = "sql7311651"
        const val PASSWORD = "cQwhQGVu9w"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_application)
    }
}

