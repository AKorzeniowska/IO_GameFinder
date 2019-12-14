package com.example.ioagh.gamefinder.models

class GameViewModel {
    var game: Game
    var gameHash: String

    constructor(game: Game, gameHash: String){
        this.game = game
        this.gameHash = gameHash
    }
}
