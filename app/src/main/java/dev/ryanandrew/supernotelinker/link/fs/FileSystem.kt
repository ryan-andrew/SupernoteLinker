package dev.ryanandrew.supernotelinker.link.fs

import android.os.Environment
import dev.ryanandrew.supernotelinker.common.log
import dev.ryanandrew.supernotelinker.link.PaintHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object FileSystem {
    suspend fun getAllFiles(
        directory: File = Environment.getExternalStorageDirectory(),
        parent: FileSystemItem.Directory? = null,
        includeEmpty: Boolean = false,
        filter: (File) -> Boolean = { it.extension == PaintHelper.PAINT_FILE_EXT }
    ): FileSystemItem.Directory? =  withContext(Dispatchers.IO) {
        val children = mutableListOf<FileSystemItem>()
        val directoryModel =
            FileSystemItem.Directory(directory.path, parent, directory.lastModified(), children)
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                val subDir = getAllFiles(file, directoryModel)
                subDir?.let { children.add(it) }
            } else {
                if (filter(file)) {
                    val fileModel = FileSystemItem.File(
                        path = file.path,
                        parent = directoryModel,
                        length = file.length(),
                        lastModified = file.lastModified()
                    )
                    children.add(fileModel)
                }
            }
        }

        if (includeEmpty || children.isNotEmpty()) directoryModel else null
    }
}


sealed class FileSystemItem {
    abstract val path: String
    abstract val lengthString: String
    abstract val length: Long
    abstract val lastModified: Long
    abstract val parent: Directory?
    open val name: String by lazy { path.substringAfterLast(java.io.File.separatorChar) }
    abstract val breadcrumbs: Sequence<FileSystemItem>
    val root: Directory
        get() = breadcrumbs.last() as Directory

    data class File(
        override val path: String,
        override val parent: Directory,
        override val length: Long,
        override val lastModified: Long,
    ) : FileSystemItem() {
        val extension: String? by lazy { name.substringAfterLast('.', "").ifEmpty { null } }
        val nameWithoutExtension: String by lazy { name.substringBeforeLast(".") }
        override val lengthString: String by lazy { length.formatFileSize() }
        override val breadcrumbs: Sequence<FileSystemItem>
            get() = generateSequence<FileSystemItem>(this) { it.parent }

        override fun toString(): String {
            return "File(path=$path, length=$length, lastModified=$lastModified)"
        }
    }

    data class Directory(
        override val path: String,
        override val parent: Directory?,
        override val lastModified: Long,
        val children: List<FileSystemItem> = mutableListOf(),
    ) : FileSystemItem() {
        override val lengthString: String by lazy {
            numChildren.let { "Total $it item${if (it == 1) "" else "s"}" }
        }

        val numChildren: Int get() = children.size

        override val length: Long by lazy {
            children.sumOf { it.length }
        }

        val isRootDir: Boolean = parent == null

        override val name: String
            get() = if (isRootDir) "Supernote" else super.name

        override val breadcrumbs: Sequence<Directory>
            get() = generateSequence(this) { it.parent }

        override fun toString(): String {
            return "Directory(path=$path, numChildren=$numChildren, lastModified=$lastModified)"
        }

        companion object {
            val empty = Directory("", null, 0)
        }
    }
}
