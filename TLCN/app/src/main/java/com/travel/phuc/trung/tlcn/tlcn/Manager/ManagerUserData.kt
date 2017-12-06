package com.travel.phuc.trung.tlcn.tlcn.Manager

import com.travel.phuc.trung.tlcn.tlcn.Login.UserData

class ManagerUserData {

    lateinit var userData : UserData
    var check: Boolean = false

    constructor()
    constructor(userData : UserData, check: Boolean){
        this.userData = userData
        this.check = check
    }
}