package dev.ryanandrew.supernotelinker.link.ui.notinstalled

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
fun AppNotInstalledScreen(
    onClose: () -> Unit,
    onInstalled: () -> Unit,
    viewModel: AppNotInstalledViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    OnResume {
        viewModel.checkAppInstalled()
    }

    LaunchedEffect(uiState) {
        if (uiState is AppNotInstalledScreenState.Installed) {
            onInstalled()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "The Atelier (drawing) app is not installed on your device",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "The app must be installed, or we cannot open the drawing files. " +
                    "Please install the Atelier app and try again. " +
                    "The app can be installed by going to:\n\n" +
                    "Settings > Apps > Supernote App Store > Atelier",
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { onClose() },
        ) {
            Text(
                text = "Close App",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
