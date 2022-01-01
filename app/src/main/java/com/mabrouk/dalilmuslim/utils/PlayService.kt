package com.mabrouk.dalilmuslim.utils

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.mabrouk.data.entities.StoryEntity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 25/08/2021 created by Just clean
 */
class PlayService : Service(), PlayerNotificationManager.NotificationListener, Player.Listener {
    @Inject
    lateinit var eventBus: EventBus
    val scope = CoroutineScope(Dispatchers.Main + Job())
    val player by lazy {
        SimpleExoPlayer.Builder(this).build()
    }
    override fun onBind(intent: Intent?): IBinder? {
      return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var items:ArrayList<StoryEntity> = ArrayList()
        intent?.extras?.apply {
            items = getParcelableArrayList<StoryEntity>(STORY_ENTITY_LIST) as ArrayList<StoryEntity>
            Log.d("efygeygfeygf",items.toString())
            val fromQuran = getBoolean(FROM_QURAN)
            initPlayer(items,fromQuran)
            Notification.showNotification(this@PlayService,items,player,1,fromQuran,this@PlayService)
        }
        return START_NOT_STICKY
    }

    private fun initPlayer(items:ArrayList<StoryEntity>,fromQuran:Boolean){
        if (fromQuran){
            items.forEach {
                player.addMediaItem(MediaItem.Builder().setUri(Uri.parse(it.url)).setMediaId(it.videoId.toString()).build())
            }
        }else {
             items.forEach {
                 player.addMediaItem(MediaItem.fromUri(it.url))
             }
        }

        player.play()
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        stopService()
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: android.app.Notification,
        ongoing: Boolean
    ) {
        if (ongoing){
          startForeground(notificationId,notification)
        }else{
            stopService()
        }
    }

    private fun stopService() {
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true)
        } else {
            stopSelf()
        }
        player.release()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        scope.launch {
            if (playWhenReady) {
               eventBus.sendStates(when (playbackState.toLong()) {
                    PlaybackStateCompat.ACTION_PLAY -> PlayerStates.Play
                    PlaybackStateCompat.ACTION_PAUSE -> PlayerStates.Pause
                    PlaybackStateCompat.STATE_SKIPPING_TO_NEXT.toLong() -> PlayerStates.Next
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS -> PlayerStates.Perv
                   else -> PlayerStates.Play
                })
            } else {
                eventBus.sendStates(PlayerStates.Play)
            }
        }

    }



    override fun onTaskRemoved(rootIntent: Intent?) {
        stopService()
        super.onTaskRemoved(rootIntent)
    }
}