package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

/**
 * Created by Admin on 16/11/2017.
 */
class GetDataTourist {
    var AnhDaiDien:String=""
    var DiaChi:String=""
    var Huyen:Int=1
    var KhuVuc:Int =1
    var Lat:Double=1.0
    var Long:Double=1.0
    var MoTa:String=""
    var Tinh:Int=1
    var idAnh:String = ""
    var idUser:String = ""
    var tenDiaDiem:String=""
    constructor()
    constructor( AnhDaiDien:String, DiaChi:String, Huyen:Int,KhuVuc:Int, Lat:Double, Long:Double, MoTa:String, Tinh:Int,idAnh:String,idUser:String,tenDiaDiem:String ){
        this.tenDiaDiem=tenDiaDiem
        this.DiaChi=DiaChi
        this.Huyen=Huyen
        this.KhuVuc = KhuVuc
        this.Lat= Lat
        this.Long=Long
        this.MoTa=MoTa
        this.Tinh=Tinh
        this.idAnh = idAnh
        this.idUser = idUser
        this.AnhDaiDien=AnhDaiDien

    }
}