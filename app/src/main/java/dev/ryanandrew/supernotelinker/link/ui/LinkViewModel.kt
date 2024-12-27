package dev.ryanandrew.supernotelinker.link.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ryanandrew.supernotelinker.link.FilePermissionManager
import dev.ryanandrew.supernotelinker.link.PaintHelper
import dev.ryanandrew.supernotelinker.link.StoredFileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor(
    private val filePermissionManager: FilePermissionManager,
    private val storedFileManager: StoredFileManager,
    private val paintHelper: PaintHelper,
) : ViewModel() {
    private val _state = MutableStateFlow<LinkActivityState>(LinkActivityState.Uninitialized)
    val state: StateFlow<LinkActivityState> = _state.asStateFlow()

    private var storedFilePath: String? = null
    private lateinit var keyword: String

    fun getInitialState(keyword: String): LinkActivityState {
        this.keyword = keyword
        return when {
            filePermissionManager.needsPermission -> LinkActivityState.Permission
            tryOpenFile(storedFileManager[keyword]) -> LinkActivityState.Finished
            else -> LinkActivityState.ChooseFile
        }
    }

    private fun tryOpenFile(filePath: String?): Boolean {
        return if (paintHelper.isPaintFileValid(filePath)) {
            storedFileManager[keyword] = filePath
            paintHelper.openDrawingFile(filePath)
            true
        } else {
            false
        }
    }

    fun onPermissionGranted() {
        if (tryOpenFile(storedFilePath)) {
            _state.value = LinkActivityState.Finished
        } else {
            _state.value = LinkActivityState.ChooseFile
        }
    }

    fun onFileChosen(filePath: String) {
        if (tryOpenFile(filePath)) {
            _state.value = LinkActivityState.Finished
        }
    }
}

@Serializable
sealed interface LinkActivityState {
    @Serializable data object Uninitialized : LinkActivityState
    @Serializable data object Permission : LinkActivityState
    @Serializable data object ChooseFile : LinkActivityState
    @Serializable data object Finished : LinkActivityState
}
