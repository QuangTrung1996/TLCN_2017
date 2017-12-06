package com.travel.phuc.trung.tlcn.tlcn.Notifications

class NotificationsData {

    lateinit var tenNguoiGui : String
    lateinit var hinhNguoiGui : String
    lateinit var noiDung : String
    lateinit var thoiGian : String

    constructor()
    constructor(tenNguoiGui : String, hinhNguoiGui : String, noiDung : String, thoiGian : String){
        this.tenNguoiGui = tenNguoiGui
        this.hinhNguoiGui = hinhNguoiGui
        this.noiDung = noiDung
        this.thoiGian = thoiGian
    }
}