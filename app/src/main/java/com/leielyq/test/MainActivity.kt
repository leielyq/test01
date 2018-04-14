package com.leielyq.test

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yhao.floatwindow.FloatWindow
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_alert.*
import android.R.attr.delay
import android.R.attr.duration
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.lzy.okgo.OkGo


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var text = TextView(this)
        setContentView(text)
//        activity_main_tv.text = String.format(resources.getString(R.string.miui), SystemUtils.isMIUI())
        val intent = Intent(this,MyService::class.java)
        startService(intent)

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5abda266");

        OkGo.getInstance().init(this.application)

        finish()
    }
}
