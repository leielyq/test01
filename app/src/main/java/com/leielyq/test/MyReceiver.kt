package com.leielyq.test

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context,intent.categories.toString()+"dsd",Toast.LENGTH_SHORT).show()
        if (intent.hasCategory("1")) {
            val contentIntent = PendingIntent.getActivity(context, 0,
                    Intent(context, MainActivity::class.java), 0)

            val remoteViews = RemoteViews("com.leielyq.test", R.layout.view_alert)
            val notification2 = NotificationCompat.Builder(context, "test")
                    .setContent(remoteViews)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2,notification2)
        }else{
            val contentIntent = PendingIntent.getActivity(context, 0,
                    Intent(context, MainActivity::class.java), 0)

            val remoteViews = RemoteViews("com.leielyq.test", R.layout.view_alert2)
            val notification2 = NotificationCompat.Builder(context, "test")
                    .setContent(remoteViews)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build()
            Glide.with(context).asBitmap().load(R.raw.hudie).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    remoteViews.setImageViewBitmap(R.id.alert_img,resource)
                }

            })


            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2,notification2)
        }

    }
}
