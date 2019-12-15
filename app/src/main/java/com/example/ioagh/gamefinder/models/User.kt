package com.example.ioagh.gamefinder.models

class User {
    var createdGames: List<String>? = ArrayList<String>()
    var joinedGames: List<String>? = ArrayList<String>()
    var localization: String? = null
    var age: Int? = null
    var name: String? = null

    constructor(createdGames: List<String>, joinedGames: List<String>, localization: String, age: Int, name: String){
        this.createdGames = createdGames
        this.joinedGames = joinedGames
        this.localization = localization
        this.age = age
        this.name = name
    }

    constructor(age:Int?, name:String?){
        this.age = age
        this.name = name
    }

    constructor(){}
}