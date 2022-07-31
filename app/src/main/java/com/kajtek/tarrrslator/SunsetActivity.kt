package com.kajtek.tarrrslator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class SunsetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sunset)
        var fragment = supportFragmentManager.findFragmentById(R.id.sunset_fragment_container)
        if (fragment == null) {
            fragment = SunsetFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.sunset_fragment_container, fragment)
                .commit()
        }

        val thread = Thread(){
            run{
                Thread.sleep(3500)
            }
            runOnUiThread(){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
        thread.start()
    }
}