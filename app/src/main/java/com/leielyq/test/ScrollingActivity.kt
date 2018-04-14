package com.leielyq.test

import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scrolling.*
import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.View.VISIBLE


class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

//        toolbar.navigationIcon=ContextCompat.getDrawable(this,R.drawable.ic_launcher_foreground)
        app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                if (state == AppBarStateChangeListener.State.EXPANDED) {
                    mtitle.visibility= View.INVISIBLE
                    //展开状态

                } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    mtitle.visibility= View.VISIBLE
                    //折叠状态

                } else {
                    mtitle.visibility= View.VISIBLE
                    //中间状态

                }
            }
        })
    }


}
