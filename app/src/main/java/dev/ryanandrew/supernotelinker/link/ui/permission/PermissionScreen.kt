package dev.ryanandrew.supernotelinker.link.ui.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ryanandrew.supernotelinker.common.OnResume

@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit,
    viewModel: PermissionScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnResume {
        viewModel.checkPermission()
    }

    LaunchedEffect(uiState) {
        if (uiState is PermissionScreenState.PermissionGranted) {
            onPermissionGranted() // Perform the side effect only once
        }
    }

    // A simple Material theme with large text and a professional layout.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "This app requires access to your files to work properly.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Please grant the necessary permissions to continue.",
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { viewModel.requestPermission() },
        ) {
            Text(
                text = "Grant Permission",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
