package com.mabrouk.dalilmuslim.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mabrouk.dalilmuslim.R
import com.mabrouk.data.entities.StoryEntity
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.mabrouk.dalilmuslim.view.MainActivity

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 24/08/2021 created by Just clean
 */
class DescriptionAdapter(
    val items: ArrayList<StoryEntity>,
    val context: Context,
    val quran: Boolean
) : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return items[player.currentWindowIndex].title
    }

    @SuppressLint("WrongConstant")
    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(FROM_QURAN, quran)
        intent.putExtra(VERSES_ID,items[player.currentWindowIndex].suraId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return if (quran) items[player.currentWindowIndex].video_key else ""
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        var bitmap: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.download)
        Glide.with(context)
            .asBitmap()
            .load(items[player.currentWindowIndex].getThumbUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.download)
                }

            })
        return bitmap
    }
}