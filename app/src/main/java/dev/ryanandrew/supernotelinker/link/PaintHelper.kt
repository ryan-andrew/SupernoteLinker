package dev.ryanandrew.supernotelinker.link

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Singleton
class PaintHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @SuppressLint("Range")
    suspend fun getPaintThumbnail(filePath: String) = withContext(Dispatchers.IO) {
        runCatching {
            SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY).use { db ->
                db.rawQuery(THUMBNAIL_QUERY, arrayOf(THUMBNAIL_SELECTION)).use { cursor ->
                    cursor.takeIf { it.moveToFirst() }
                        ?.getBlob(cursor.getColumnIndex(THUMBNAIL_COLUMN_NAME))
                        ?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                }
            }
        }.onFailure { it.printStackTrace() }.getOrNull()
    }

    fun openDrawingFile(filePath: String) {
        val intent = Intent()
            .putExtra(FILE_PATH_EXTRA_KEY, filePath)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setComponent(ComponentName(PAINT_PACKAGE, PAINT_CLASS))
        context.startActivity(intent)
    }

    val isPaintAppInstalled: Boolean
        get () = runCatching {
            context.packageManager.getPackageInfo(PAINT_PACKAGE, 0)
            true
        }.getOrDefault(false)

    @OptIn(ExperimentalContracts::class)
    fun isPaintFileValid(filePath: String?): Boolean {
        contract {
            returns(true) implies (filePath != null)
        }

        return runCatching {
            val path = filePath ?: return@runCatching false
            File(path).run { exists() && isFile && extension.lowercase() == PAINT_FILE_EXT }
        }.getOrElse { false }
    }

    companion object {
        const val PAINT_FILE_EXT = "spd"

        private const val THUMBNAIL_SELECTION = "thumbnail"
        private const val THUMBNAIL_COLUMN_NAME = "value"
        private const val THUMBNAIL_QUERY = "SELECT value FROM config WHERE name=?"

        private const val PAINT_PACKAGE = "com.ratta.supernote.paint"
        private const val PAINT_CLASS = "com.ratta.paint.MainActivity"

        private const val FILE_PATH_EXTRA_KEY = "file_path"
    }
}