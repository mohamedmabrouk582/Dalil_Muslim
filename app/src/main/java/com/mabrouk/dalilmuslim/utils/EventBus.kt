package com.mabrouk.dalilmuslim.utils

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {
   private val downloadType =  MutableSharedFlow<String>()
   private val playerStates = MutableSharedFlow<PlayerStates>()

   private val channel = Channel<String>()

   suspend fun send(name:String){
        channel.send(name)
    }

   suspend fun recieve() : Flow<String>{
       return channel.consumeAsFlow()
    }

    suspend fun sendType(name:String){
        downloadType.emit(name)
    }

     fun receiveType() : SharedFlow<String>{
       return downloadType.asSharedFlow()
    }

    suspend fun sendStates(states:PlayerStates){
        playerStates.emit(states)
    }

    fun receiveState() : SharedFlow<PlayerStates>{
        return playerStates.asSharedFlow()
    }


}