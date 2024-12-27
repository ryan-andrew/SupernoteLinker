package dev.ryanandrew.supernotelinker.link.ui.notinstalled

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ryanandrew.supernotelinker.link.PaintHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppNotInstalledViewModel @Inject constructor(
    private val paintHelper: PaintHelper
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<AppNotInstalledScreenState>(AppNotInstalledScreenState.NotInstalled)
    val uiState: StateFlow<AppNotInstalledScreenState> get() = _uiState

    fun checkAppInstalled() {
        if (paintHelper.isPaintAppInstalled) {
            _uiState.value = AppNotInstalledScreenState.Installed
        } else {
            _uiState.value = AppNotInstalledScreenState.NotInstalled
        }
    }
}

sealed interface AppNotInstalledScreenState {
    data object NotInstalled : AppNotInstalledScreenState
    data object Installed : AppNotInstalledScreenState
}
