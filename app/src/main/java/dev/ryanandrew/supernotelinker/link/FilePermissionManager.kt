package dev.ryanandrew.supernotelinker.link

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilePermissionManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    @get:ChecksSdkIntAtLeast(api = 30)
    val needsPermission: Boolean get() =
        Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()

    @RequiresApi(Build.VERSION_CODES.R)
    fun grantFilePermission() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .setData(Uri.parse("package:${context.packageName}"))
        context.startActivity(intent)
    }
}
