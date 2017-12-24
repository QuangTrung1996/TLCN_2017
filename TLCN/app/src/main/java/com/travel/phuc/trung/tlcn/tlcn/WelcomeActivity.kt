package com.travel.phuc.trung.tlcn.tlcn

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar!!.hide()
        startMainActivity()
    }

    private fun startMainActivity() {
        val thread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)// set thoi gian dong Activity
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)


                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }
}
