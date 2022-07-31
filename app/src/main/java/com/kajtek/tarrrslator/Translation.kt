package com.kajtek.tarrrslator

class Translation {
    var id : Int? = null
    var login : String? = null
    var english : String? = null
    var pirate : String? = null

    constructor(){}

    constructor(id:Int, login:String, english:String, pirate:String){
        this.id = id
        this.login = login
        this.english = english
        this.pirate = pirate
    }
}