package dev.ryanandrew.supernotelinker.link.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ryanandrew.supernotelinker.common.keyword
import dev.ryanandrew.supernotelinker.common.log
import dev.ryanandrew.supernotelinker.link.FilePermissionManager
import dev.ryanandrew.supernotelinker.link.PaintHelper
import dev.ryanandrew.supernotelinker.link.ui.permission.PermissionScreen
import dev.ryanandrew.supernotelinker.link.ui.paint.PaintFileBrowser
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

@AndroidEntryPoint
class LinkActivity : ComponentActivity() {
    private val viewModel: LinkViewModel by viewModels()

    @Inject
    lateinit var paintHelper: PaintHelper

    @Inject
    lateinit var filePermissionManager: FilePermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialState = viewModel.getInitialState(keyword!!)
        if (initialState is LinkActivityState.Finished) {
            finish()
            return
        }
        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                viewModel.state.filterNot {
                    it is LinkActivityState.Uninitialized
                }.collect { state ->
                    when (state) {
                        LinkActivityState.Finished -> finish()
                        else -> navController.navigate(state)
                    }
                }
            }

            NavHost(navController = navController, startDestination = initialState) {
                composable<LinkActivityState.Permission> {
                    PermissionScreen(
                        filePermissionManager = filePermissionManager,
                        onPermissionGranted = {
                            viewModel.onPermissionGranted()
                        }
                    )
                }
                composable<LinkActivityState.ChooseFile> {
                    PaintFileBrowser(
                        onFileChosen = {
                            viewModel.onFileChosen(it)
                        },
                        onCancel = {
                            viewModel.onCancel()
                        }
                    )
                }
            }
        }
    }
}
