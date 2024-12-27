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
import dev.ryanandrew.supernotelinker.link.PaintHelper
import dev.ryanandrew.supernotelinker.link.ui.permission.PermissionScreen
import dev.ryanandrew.supernotelinker.link.ui.paint.PaintFileBrowser
import javax.inject.Inject

@AndroidEntryPoint
class LinkActivity : ComponentActivity() {
    private val viewModel: LinkViewModel by viewModels()

    @Inject
    lateinit var paintHelper: PaintHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialState = viewModel.init(keyword!!)
        if (initialState is LinkActivityState.Finished) {
            finish()
            return
        }
        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                viewModel.state.collect { state ->
                    when (state) {
                        LinkActivityState.Finished -> finish()
                        else -> navController.navigate(state)
                    }
                }
            }

            NavHost(navController = navController, startDestination = initialState) {
                composable<LinkActivityState.Permission> {
                    PermissionScreen(
                        onPermissionGranted = {
                            viewModel.onPermissionGranted()
                        }
                    )
                }
                composable<LinkActivityState.ChooseFile> {
                    PaintFileBrowser(
                        onFileChosen = {
                            viewModel.onFileChosen(it)
                        }
                    )
                }
            }
        }
    }
}
