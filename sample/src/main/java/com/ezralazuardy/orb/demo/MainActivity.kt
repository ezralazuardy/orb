/*
 * Created by Ezra Lazuardy on 8/31/20 4:51 PM
 * Copyright (c) 2020. All rights reserved.
 * Last modified 5/6/20 8:23 AM
 */

package com.ezralazuardy.orb.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ezralazuardy.orb.Orb
import com.ezralazuardy.orb.OrbHelper.orbObserver
import com.ezralazuardy.orb.OrbListener
import com.ezralazuardy.orb.OrbResponse
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), OrbListener {

    private lateinit var orb: Orb
    private lateinit var observer: (OrbResponse) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observer = orbObserver {
            state.text = it.state.toString()
            type.text = it.type.toString()
            errorMessage.text = it.errorMessage.toString()
        }
        orb = Orb.with(this).setListener(this).observe(observer)
        buttonToggleOrb.apply { isChecked = true }.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) orb.observe(observer) else orb.stop()
        }
    }

    override fun onOrbObserve() {
        log.text = "Start observing Orb..."
    }

    override fun onOrbActive() {
        log.text = "Orb activated"
    }

    override fun onOrbInactive() {
        log.text = "Orb is inactive"
    }

    override fun onOrbStop() {
        buttonToggleOrb.isChecked = false
        log.text = "Orb has been stopped"
    }
}