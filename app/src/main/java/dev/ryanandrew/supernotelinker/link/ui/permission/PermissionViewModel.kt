package dev.ryanandrew.supernotelinker.link.ui.permission

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ryanandrew.supernotelinker.link.FilePermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionScreenViewModel @Inject constructor(
    private val filePermissionManager: FilePermissionManager,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<PermissionScreenState>(PermissionScreenState.NeedsPermission)
    val uiState: StateFlow<PermissionScreenState> get() = _uiState

    init {
        checkPermission()
    }

    fun checkPermission() {
        if (filePermissionManager.needsPermission) {
            _uiState.value = PermissionScreenState.NeedsPermission
        } else {
            _uiState.value = PermissionScreenState.PermissionGranted
        }
    }

    @SuppressLint("NewApi") // TODO
    fun requestPermission() {
        filePermissionManager.grantFilePermission()
    }
}

sealed interface PermissionScreenState {
    data object NeedsPermission : PermissionScreenState
    data object PermissionGranted : PermissionScreenState
}
