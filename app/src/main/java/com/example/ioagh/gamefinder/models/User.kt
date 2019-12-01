package com.example.ioagh.gamefinder.models

class User {
    var createdGames: List<String>? = null
    var previousGames: List<String>? = null
    var futureGames: List<String>? = null
    var localization: String? = null

    constructor(createdGames: List<String>, previousGames: List<String>, futureGames: List<String>, localization: String){
        this.createdGames = createdGames
        this.previousGames = previousGames
        this.futureGames = futureGames
        this.localization = localization
    }
}