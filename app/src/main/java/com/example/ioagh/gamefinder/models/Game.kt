package com.example.ioagh.gamefinder.models

import com.example.ioagh.gamefinder.providers.parseStringToDate
import java.util.*

class Game {
    var gameName: String? = null
    var localization: String? = null
    var gameKind: Int? = null
    var gameTypes: List<Int>? = null
    var gamers: Int? = null
    var date: String? = null
    var durationInMinutes: Int? = null
    var owner: String? = null

    constructor(gameName: String, localization: String, gameKind: Int, gameTypes: List<Int>, gamers: Int, date: String, durationInMinutes: Int, owner: String){
        this.gameName = gameName
        this.localization = localization
        this.gameKind = gameKind
        this.gameTypes = gameTypes
        this.gamers = gamers
        this.date = date
        this.durationInMinutes = durationInMinutes
        this.owner = owner
    }

    companion object GameKinds {
        const val GAME_PUB = 1
        const val OWNER = 2
    }
}

