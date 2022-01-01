package com.mabrouk.dalilmuslim.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mabrouk.dalilmuslim.R
import com.mabrouk.data.entities.StoryEntity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 24/08/2021 created by Just clean
 */
class Notification {

    companion object{
        fun showNotification(context: Context,item: ArrayList<StoryEntity>,player:ExoPlayer,notificationId: Int,quran:Boolean = false,listener:PlayerNotificationManager.NotificationListener = object : PlayerNotificationManager.NotificationListener{

        }) : PlayerNotificationManager{
            val notificationManager =  (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            val channelId = "channelId"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Foo channel name"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, name, importance)
                notificationManager.createNotificationChannel(channel)
            }

            val builder = PlayerNotificationManager.Builder(context,notificationId,channelId).also {
                it.setMediaDescriptionAdapter(DescriptionAdapter(item,context,quran))
                it.setChannelImportance(NotificationUtil.IMPORTANCE_HIGH)
                it.setSmallIconResourceId(R.drawable.logo)
            }
            builder.setNotificationListener(listener)
            builder.setNotificationListener(object : PlayerNotificationManager.NotificationListener{
            })
            builder.build().setPlayer(player)
            return builder.build()
        }


    }
}