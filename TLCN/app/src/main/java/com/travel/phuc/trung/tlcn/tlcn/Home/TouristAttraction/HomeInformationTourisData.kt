package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

/**
 * Created by Admin on 16/11/2017.
 */
import java.io.Serializable
// lớp chứa thông tin của dịa điểm dulich
data class HomeInformationTourisData constructor(var key:String,var Lat:Double,var Long:Double,var Mota:String,var TenDiaDiem:String, var DiaChi:String, var url:String,var LuocLike:Int, var SoCmmt:Int,var DanhGia:Float):Serializable{}