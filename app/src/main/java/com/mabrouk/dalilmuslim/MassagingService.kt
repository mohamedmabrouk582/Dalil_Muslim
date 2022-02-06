package com.mabrouk.dalilmuslim

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 20/01/2022 created by Just clean
 */
class MassagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("onMessageReceived",p0.toString())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

}