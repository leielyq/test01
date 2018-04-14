package com.leielyq.test

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.google.gson.Gson
import com.iflytek.cloud.RecognizerResult
import com.yhao.floatwindow.FloatWindow
import com.yhao.floatwindow.IFloatWindow
import com.iflytek.cloud.thirdparty.w
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.iflytek.cloud.SpeechConstant
import com.iflytek.sunflower.FlowerCollector.setParameter
import com.iflytek.cloud.ui.RecognizerDialog
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.Callback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response


class MyService : Service() {
    var iFloatWindow: IFloatWindow? = null
    var mTV: TextView? = null
    var mIV: ImageView? = null

    override fun onCreate() {
        super.onCreate()
        val contentIntent = PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), 0)

        val intent1 = PendingIntent.getBroadcast(this,1,Intent(this,MyReceiver::class.java).addCategory("1"),0)
        val intent2 = PendingIntent.getBroadcast(this,1,Intent(this,MyReceiver::class.java).addCategory("2"),0)
        val action1 = NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "有问题", intent1).build()
        val action2 = NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "没问题", intent2).build()
        val notification = NotificationCompat.Builder(this, "test")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(Color.BLUE)
                .setTicker("Foreground Service Start")
                .setContentTitle("小蛋正在活动中")
                .setContentText("正在摇尾巴")
                .addAction(action1)
                .addAction(action2)
                .build()


        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val con = LayoutInflater.from(this).inflate(R.layout.view_alert, null)
        con.setOnClickListener {
            initSpeech(this.applicationContext)
        }
        mTV = con.findViewById(R.id.alert_tv)
        mIV = con.findViewById(R.id.alert_img)

        Glide.with(this).load(R.raw.huli).into(mIV!!)
        FloatWindow
                .with(this.applicationContext)
                .setView(con)
                .setDesktopShow(true)
                .build()

        con.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                Log.d("ceshi", view.x.toString() + "")
                return false
            }
        })

        iFloatWindow = FloatWindow.get()
        iFloatWindow?.show()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        FloatWindow.destroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented") as Throwable
    }

    /**
     * 初始化语音识别
     */
    fun initSpeech(context: Context) {

        var kqwSpeechCompound = KqwSpeechCompound(context)
        //1.创建RecognizerDialog对象
        val mDialog = RecognizerDialog(context, null)
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin")
        //3.设置回调接口
        mDialog.setListener(object : RecognizerDialogListener {
            override fun onResult(recognizerResult: RecognizerResult, isLast: Boolean) {
                if (!isLast) {
                    //解析语音
                    val result = parseVoice(recognizerResult.getResultString())

                    mTV!!.text = result

                    val gson = Gson()

                    val postBean = PostBean()
                    val per = PostBean.PerceptionBean()
                    val input = PostBean.PerceptionBean.InputTextBean()
                    input.text = result

                    per.inputText = input
                    val user = PostBean.UserInfoBean()
                    user.apiKey = "c7481e79c63b4dad88df28b6f82cccd7"
                    user.userId = "1232312312312321"
                    postBean.perception = per
                    postBean.userInfo = user

                    val post = gson.toJson(postBean)

                    OkGo.post<String>("http://openapi.tuling123.com/openapi/api/v2")
                            .upJson(post)
                            .execute(object : StringCallback() {
                                override fun onSuccess(response: Response<String>?) {
                                    val fromJson = gson.fromJson<ResultBean>(response!!.body(), ResultBean::class.java)
                                    var string = fromJson!!.results[0].values.text.toString()
                                    kqwSpeechCompound.speaking(string)
                                    mTV!!.text = string
                                }

                            })
                }
            }

            override fun onError(speechError: SpeechError) {

            }
        })

        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //4.显示dialog，接收语音输入
        mDialog.show()
    }

    /**
     * 解析语音json
     */
    fun parseVoice(resultString: String): String {
        val gson = Gson()
        val voiceBean = gson.fromJson(resultString, Voice::class.java)

        val sb = StringBuffer()
        val ws = voiceBean.ws
        for (wsBean in ws!!) {
            val word = wsBean.cw!!.get(0).w
            sb.append(word)
        }
        return sb.toString()
    }

    /**
     * 语音对象封装
     */
    inner class Voice {

        var ws: ArrayList<WSBean>? = null

        inner class WSBean {
            var cw: ArrayList<CWBean>? = null
        }

        inner class CWBean {
            var w: String? = null
        }
    }
}
