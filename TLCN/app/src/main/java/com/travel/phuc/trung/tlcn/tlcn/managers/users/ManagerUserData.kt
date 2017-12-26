package com.travel.phuc.trung.tlcn.tlcn.managers.users

import com.travel.phuc.trung.tlcn.tlcn.logins.UserData

class ManagerUserData {

    lateinit var userData : UserData
    var check: Boolean = false

    constructor()
    constructor(userData : UserData, check: Boolean){
        this.userData = userData
        this.check = check
    }
}