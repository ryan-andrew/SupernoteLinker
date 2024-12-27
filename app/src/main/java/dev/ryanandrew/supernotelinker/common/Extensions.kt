package dev.ryanandrew.supernotelinker.common

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

val Activity.keyword: String? get() = intent?.data?.path?.substring(1)
val Activity.command: String? get() = intent?.data?.host

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermissions(vararg permissions: String): Boolean {
    return permissions.all { hasPermission(it) }
}

fun Activity.requestLegacyPermissions(vararg permissions: String) {
    ActivityCompat.requestPermissions(
        this,
        permissions,
        0
    )
}
