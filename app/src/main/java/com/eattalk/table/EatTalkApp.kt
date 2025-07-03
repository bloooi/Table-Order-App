package com.eattalk.table

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import com.eattalk.table.api.websocket.WebSocketService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EatTalkApp: Application(){
    override fun onCreate() {
        super.onCreate()
        ContextCompat.startForegroundService(this, Intent(this, WebSocketService::class.java))
    }
}