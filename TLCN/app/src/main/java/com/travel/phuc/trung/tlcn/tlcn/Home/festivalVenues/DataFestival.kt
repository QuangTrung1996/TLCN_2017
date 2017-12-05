package com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues

import java.io.Serializable

/**
 * Created by Admin on 4/12/2017.
 */
data class DataFestival constructor(var key:String,var Lat:Double,var Long:Double,var Mota:String,var TenDiaDiem:String, var DiaChi:String, var url:String,var NgayBD:Long,var NgayKT:Long): Serializable {}
