package com.ezralazuardy.orb.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ezralazuardy.orb.Orb
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Orb.with(this).observe(this, Observer {
            label.text = if(it.connected) "Connected!" else  "Disconnected :("
        })
    }
}