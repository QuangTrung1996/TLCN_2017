package com.travel.phuc.trung.tlcn.tlcn.Conect

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast



class CheckInternet constructor(var internet: CheckInternetInterface) {
    var a:CheckInternetInterface=internet
    fun checkConnection(context: Context) {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connMgr.activeNetworkInfo

        if (activeNetworkInfo != null) {
            //Toast.makeText(context, activeNetworkInfo.typeName, Toast.LENGTH_SHORT).show()
            // niếu có internet gọi funtion kientrainternet trong interface truyền vào biến true
            a.kiemtrainternet(true)

            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                // a.kiemtrainternet(true)
            } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                // a.kiemtrainternet(true)
            }
        }
        else {
            // niếu ko có internet gọi funtion kientrainternet trong interface truyền vào biến false
            a.kiemtrainternet(false)
        }
    }
}