package com.travel.phuc.trung.tlcn.tlcn.managers

class ManagerTouristData {

    var AnhDaiDien  :String=""
    var DiaChi      :String=""
    var Huyen       :Int=1
    var Lat         :Double=1.0
    var Long        :Double=1.0
    var MoTa        :String=""
    var Tinh        :Int=1
    var tenDiaDiem  :String=""
    var idUser      : String =""

    constructor()

    constructor(AnhDaiDien: String, DiaChi: String, Huyen: Int, Lat: Double, Long: Double, MoTa: String, Tinh: Int, tenDiaDiem: String, idUser: String){
        this.tenDiaDiem = tenDiaDiem
        this.DiaChi     = DiaChi
        this.Huyen      = Huyen
        this.Lat        = Lat
        this.Long       = Long
        this.MoTa       = MoTa
        this.Tinh       = Tinh
        this.AnhDaiDien = AnhDaiDien
        this.idUser     = idUser
    }
}