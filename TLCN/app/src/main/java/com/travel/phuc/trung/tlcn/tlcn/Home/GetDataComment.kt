package com.travel.phuc.trung.tlcn.tlcn.Home

// lớp nhận thông tin từ firebase
class GetDataComment {
    var id_User:String=""
    var text:String=""
    var time:Long=1232
    constructor()
    constructor(id_User:String,text:String,time:Long){
        this.id_User=id_User
        this.text=text
        this.time=time
    }
}