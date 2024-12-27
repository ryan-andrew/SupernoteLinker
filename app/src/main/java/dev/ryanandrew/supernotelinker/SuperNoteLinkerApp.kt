package dev.ryanandrew.supernotelinker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dev.ryanandrew.supernotelinker.common.log

@HiltAndroidApp
class SuperNoteLinkerApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        log("OPENING APP")
    }
}