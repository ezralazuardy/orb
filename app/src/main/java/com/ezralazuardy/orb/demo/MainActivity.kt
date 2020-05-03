/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:42 AM
 */

package com.ezralazuardy.orb.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ezralazuardy.orb.Orb
import com.ezralazuardy.orb.OrbListener
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), OrbListener {

    private lateinit var orb: Orb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        orb = Orb.with(this)
        observeOrb()
        buttonToggleOrb.apply { isChecked = true }.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) observeOrb() else orb.stop()
        }
    }

    private fun observeOrb() = orb.setListener(this).observe {
        state.text = it.state.toString()
        type.text = it.type.toString()
        errorMessage.text = it.errorMessage.toString()
    }

    override fun onOrbCreate() {
        log.text = "Creating Orb..."
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

    override fun onOrbStop(stopped: Boolean) {
        if (stopped) {
            buttonToggleOrb.isChecked = false
            log.text = "Orb has been stopped"
        } else log.text = "Error happened when stopping Orb"
    }
}