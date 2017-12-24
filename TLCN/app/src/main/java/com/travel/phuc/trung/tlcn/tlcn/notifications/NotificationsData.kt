package com.travel.phuc.trung.tlcn.tlcn.notifications

class NotificationsData {

    lateinit var tenNguoiGui : String
    lateinit var hinhNguoiGui : String
    lateinit var noiDung : String
    var thoiGian : Long = 0
    var order : Long = 0

    constructor()
    constructor(tenNguoiGui : String, hinhNguoiGui : String, noiDung : String, thoiGian : Long){
        this.tenNguoiGui = tenNguoiGui
        this.hinhNguoiGui = hinhNguoiGui
        this.noiDung = noiDung
        this.thoiGian = thoiGian
        this.order = 0 - thoiGian
    }
}