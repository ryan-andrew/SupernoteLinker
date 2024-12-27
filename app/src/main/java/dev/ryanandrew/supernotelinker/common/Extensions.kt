package dev.ryanandrew.supernotelinker.common

import android.app.Activity

val Activity.keyword: String? get() = intent?.data?.path?.substring(1)
val Activity.command: String? get() = intent?.data?.host
