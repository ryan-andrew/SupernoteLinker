package dev.ryanandrew.supernotelinker.link.fs

import kotlin.math.log10
import kotlin.math.pow

private val units = arrayOf("B", "KB", "MB", "GB")

internal fun Long.formatFileSize(): String {
    if (this <= 0) return "0B"
    val unitIndex = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return "${this / 1024.0.pow(unitIndex.toDouble()).toInt()}${units[unitIndex]}"
}
