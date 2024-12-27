package dev.ryanandrew.supernotelinker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SuperNoteLinkerApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
