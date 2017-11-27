package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.widget.Toast
import java.lang.ClassCastException

/**
 * Created by Admin on 21/11/2017.
 */
class DeteiladFragmentDialogDisklike :DialogFragment() {

      lateinit var CheckDislike:DeteiladInterfaceDisklike
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        val dialog:AlertDialog.Builder = AlertDialog.Builder(this.activity)
        dialog.setTitle("Xác Nhận")
        dialog.setMessage("tôi không thích địa điểm này và không muốn nhìn thấy nó trên thông tin tìm kíếm")
        dialog.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
            if (CheckDislike == null){
                Toast.makeText(activity,"ngủ",Toast.LENGTH_SHORT).show()
                CheckDislike!!.check(true)
            }
        })
        dialog.setNegativeButton("Không", DialogInterface.OnClickListener{ dialogInterface, i ->

        })
        val  hopthoai:Dialog = dialog.create()
        return hopthoai
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            CheckDislike = context as DeteiladInterfaceDisklike
        }catch (e:ClassCastException){
            e.printStackTrace()
        }
    }
}

