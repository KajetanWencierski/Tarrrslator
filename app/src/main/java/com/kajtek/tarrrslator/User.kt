package com.kajtek.tarrrslator

class User {
    var login : String? = null
    var password : String? = null

    constructor(){}

    constructor(login:String, password:String){
        this.login = login
        this.password = password
    }
}