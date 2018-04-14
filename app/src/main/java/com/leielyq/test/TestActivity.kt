package com.leielyq.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val view = View(this)
        view.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {

            }
            false
        }
    }
}
