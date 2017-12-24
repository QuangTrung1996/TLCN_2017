package com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues

/**
 * Created by Admin on 4/12/2017.
 */
class getDataFestival {
    var AnhDaiDien:String=""
    var DiaChi:String=""
    var Huyen:Int=1
    var KhuVuc:Int=1
    var Lat:Double=1.0
    var Long:Double=1.0
    var MoTa:String=""
    var NgayBD:Long=1
    var NgayKT:Long=1
    var TenLeHoi:String=""
    var Tinh:Int=1
    var idAnh:String = ""
    var idUser : String =""
    constructor()
    constructor( AnhDaiDien:String, DiaChi:String, Huyen:Int,KhuVuc:Int, Lat:Double, Long:Double, MoTa:String, NgayBD:Long,NgayKT:Long, TenLeHoi:String ,Tinh:Int,idAnh:String,idUser:String){
        this.TenLeHoi=TenLeHoi
        this.KhuVuc = KhuVuc
        this.NgayBD=NgayBD
        this.NgayKT = NgayKT
        this.DiaChi=DiaChi
        this.Huyen=Huyen
        this.Lat= Lat
        this.Long=Long
        this.MoTa=MoTa
        this.Tinh=Tinh
        this.AnhDaiDien=AnhDaiDien
        this.idAnh = idAnh
        this.idUser = idUser

    }
}