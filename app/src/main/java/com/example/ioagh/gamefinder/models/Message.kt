package com.example.ioagh.gamefinder.models

class Message {
    private lateinit var text: String // message body
    private lateinit var memberData: MemberData // data of the user that sent this message
    private var belongsToCurrentUser: Boolean = false // is this message sent by us?

    constructor(text: String, memberData: MemberData, belongsToCurrentUser: Boolean) {
        this.text = text
        this.memberData = memberData
        this.belongsToCurrentUser = belongsToCurrentUser
    }

    constructor(){}

    fun getText(): String {
        return text
    }

    fun getMemberData(): MemberData {
        return memberData
    }

    fun isBelongsToCurrentUser(): Boolean {
        return belongsToCurrentUser
    }
}