package com.example.ioagh.gamefinder.models


class Game {
    var gameName: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var gameKind: Int? = null
    var gameTypes: List<Int>? = null
    var players: Int? = null
    var maxPlayers: Int? = null
    var date: String? = null
    var durationInMinutes: Int? = null
    var owner: String? = null
    var description: String? = null

    constructor() {}

    constructor(
        gameName: String?,
        latitude: Double?,
        longitude: Double?,
        gameKind: Int?,
        gameTypes: List<Int>?,
        players: Int?,
        maxPlayers: Int?,
        date: String?,
        durationInMinutes: Int?,
        owner: String?
    ) {
        this.gameName = gameName
        this.latitude = latitude
        this.longitude = longitude
        this.gameKind = gameKind
        this.gameTypes = gameTypes
        this.players = players
        this.maxPlayers = maxPlayers
        this.date = date
        this.durationInMinutes = durationInMinutes
        this.owner = owner
    }

    constructor(
        gameName: String?,
        latitude: Double?,
        longitude: Double?,
        gameKind: Int?,
        gameTypes: List<Int>?,
        players: Int?,
        maxPlayers: Int?,
        date: String?,
        durationInMinutes: Int?,
        owner: String?,
        description: String?
    ) {
        this.gameName = gameName
        this.latitude = latitude
        this.longitude = longitude
        this.gameKind = gameKind
        this.gameTypes = gameTypes
        this.players = players
        this.maxPlayers = maxPlayers
        this.date = date
        this.durationInMinutes = durationInMinutes
        this.owner = owner
        this.description = description
    }


    companion object GameKinds {
        const val GAME_PUB = 1
        const val OWNER = 2
    }
}

