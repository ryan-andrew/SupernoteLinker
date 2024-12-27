package dev.ryanandrew.supernotelinker.link.ui.paint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ryanandrew.supernotelinker.link.fs.FileSystem
import dev.ryanandrew.supernotelinker.link.fs.FileSystemItem
import dev.ryanandrew.supernotelinker.link.fs.SortBy
import dev.ryanandrew.supernotelinker.link.fs.SortMode
import dev.ryanandrew.supernotelinker.link.fs.sorted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintFileBrowserViewModel @Inject constructor() : ViewModel() {
    private val numItemsPerPage = 6

    private val _currentDirectory = MutableStateFlow(FileSystemItem.Directory.empty)
    val currentDirectory: StateFlow<FileSystemItem.Directory> get() = _currentDirectory

    private val _visibleItems = MutableStateFlow<List<FileSystemItem>>(emptyList())
    val visibleItems: StateFlow<List<FileSystemItem>> get() = _visibleItems

    private val _currentPageIndex = MutableStateFlow(1)
    val currentPageIndex: StateFlow<Int> get() = _currentPageIndex

    val numPagesTotal = currentDirectory.map { (it.numChildren / 6) + 1 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    private var sortMode = SortMode(SortBy.NAME, false)

    init {
        navigateToDirectory(null)
    }

    private val currentSortedChildren: List<FileSystemItem>
        get() = _currentDirectory.value.children.sorted(sortMode)

    fun setSortMode(sortBy: SortBy, reverse: Boolean) {
        sortMode = SortMode(sortBy, reverse)
    }

    fun navigateToDirectory(directory: FileSystemItem.Directory?) {
        viewModelScope.launch {
            _currentDirectory.value = directory ?: FileSystem.getAllFiles()!!
            _visibleItems.value = currentSortedChildren.take(numItemsPerPage)
            _currentPageIndex.value = 1
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            check(_currentPageIndex.value < numPagesTotal.value) {
                "Cannot load next page when there are no more pages to load!"
            }
            val numItemsOnAllPreviousPages = (_currentPageIndex.value - 1) * numItemsPerPage
            val alreadyLoaded = _visibleItems.value.size + numItemsOnAllPreviousPages
            val nextPage = currentSortedChildren.drop(alreadyLoaded).take(numItemsPerPage)
            _visibleItems.value = nextPage
            _currentPageIndex.value += 1
        }
    }

    fun loadPreviousPage() {
        viewModelScope.launch {
            check(_currentPageIndex.value > 1) {
                "Cannot load previous page when on first page!"
            }
            val previousPageStartIndex = (_currentPageIndex.value - 2) * numItemsPerPage
            val previousPageItems = currentSortedChildren
                .drop(previousPageStartIndex)
                .take(numItemsPerPage)
            _visibleItems.value = previousPageItems
            _currentPageIndex.value -= 1
        }
    }
}
