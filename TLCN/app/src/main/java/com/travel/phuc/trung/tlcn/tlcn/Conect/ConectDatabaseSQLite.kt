package com.travel.phuc.trung.tlcn.tlcn.Conect

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Admin on 16/11/2017.
 */
class ConectDatabaseSQLite {
    fun initDatabase(activity: Activity, databaseName: String): SQLiteDatabase {
        try {
            val outFileName = activity.applicationInfo.dataDir + "/databases/" + databaseName
            val f = File(outFileName)
            if (!f.exists()) {
                val e = activity.assets.open(databaseName)
                val folder = File(activity.applicationInfo.dataDir + "/databases/")
                if (!folder.exists()) {
                    folder.mkdir()
                }
                val myOutput = FileOutputStream(outFileName)
                val buffer = ByteArray(1024)

                var length: Int
                length = e.read(buffer)
                while (length > 0) {
                    myOutput.write(buffer, 0, length)
                    length = e.read(buffer)
                }

                myOutput.flush()
                myOutput.close()
                e.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null)
    }
}