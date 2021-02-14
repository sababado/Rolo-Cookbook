package com.sababado.rolo.cookbook.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sababado.rolo.cookbook.utils.ext.startActivity
import com.sababado.rolocookbook.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<View>(R.id.location_button).setOnClickListener { startActivity<LocationActivity>() }
    }
}