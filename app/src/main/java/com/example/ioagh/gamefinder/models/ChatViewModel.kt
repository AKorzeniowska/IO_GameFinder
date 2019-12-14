package com.example.ioagh.gamefinder.models

class ChatViewModel {
    var gameHash: String
    var gameName: String
    var gameDate: String

    constructor(gameHash: String, gameName: String, gameDate: String){
        this.gameHash = gameHash
        this.gameName = gameName
        this.gameDate = gameDate
    }
}