package dev.ryanandrew.supernotelinker.link

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ryanandrew.supernotelinker.common.hasPermissions
import dev.ryanandrew.supernotelinker.common.requestLegacyPermissions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilePermissionManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val needsPermission: Boolean get() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            !Environment.isExternalStorageManager()
        }
        else -> {
            !context.hasPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    fun grantFilePermission(activity: Activity) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setData(Uri.parse("package:${context.packageName}"))
            context.startActivity(intent)
        }
        else -> {
            activity.requestLegacyPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }
}
