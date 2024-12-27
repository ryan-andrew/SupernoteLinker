package dev.ryanandrew.supernotelinker.link.fs

enum class SortBy {
    NAME,
    SIZE,
    DATE
}

data class SortMode(val sortBy: SortBy, val reverse: Boolean)

internal fun <T : FileSystemItem> List<T>.sorted(sortMode: SortMode): List<T> {
    return sortedWith(
        directoriesFirstComparator.thenComparing(
            when (sortMode.sortBy) {
                SortBy.NAME -> nameComparator
                SortBy.SIZE -> sizeComparator
                SortBy.DATE -> dateComparator
            }.reverse(sortMode.reverse)
        )
    )
}

private val directoriesFirstComparator: Comparator<FileSystemItem> = Comparator { a, b ->
    when {
        a is FileSystemItem.Directory && b !is FileSystemItem.Directory -> -1
        a !is FileSystemItem.Directory && b is FileSystemItem.Directory -> 1
        else -> 0
    }
}

private val nameComparator = Comparator<FileSystemItem> { a, b ->
    a.name.compareTo(b.name)
}

private val sizeComparator = Comparator<FileSystemItem> { a, b ->
    a.length.compareTo(b.length)
}

private val dateComparator = Comparator<FileSystemItem> { a, b ->
    a.lastModified.compareTo(b.lastModified)
}

internal fun <T> Comparator<T>.reverse(reverse: Boolean) =
    if (reverse) reversed() else this
