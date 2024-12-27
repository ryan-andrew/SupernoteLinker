package dev.ryanandrew.supernotelinker.link

import android.app.Activity.MODE_PRIVATE
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoredFileManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val storedFiles by lazy {
        context.getSharedPreferences("StoredFilePaths", MODE_PRIVATE)
    }

    operator fun set(keyword: String, filePath: String) {
        storedFiles.edit().putString(keyword, filePath).apply()
    }

    operator fun get(keyword: String): String? {
        return storedFiles.getString(keyword, null)
    }
}
