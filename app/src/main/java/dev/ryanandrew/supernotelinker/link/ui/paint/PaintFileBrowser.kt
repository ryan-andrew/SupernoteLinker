package dev.ryanandrew.supernotelinker.link.ui.paint

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ryanandrew.supernotelinker.R
import dev.ryanandrew.supernotelinker.link.fs.FileSystemItem

@Composable
fun PaintFileBrowser(
    onFileChosen: (String) -> Unit,
    onCancel: () -> Unit,
    viewModel: PaintFileBrowserViewModel = hiltViewModel()
) {
    val currentDirectory by viewModel.currentDirectory.collectAsState()
    val visibleItems by viewModel.visibleItems.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header()

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)

        Breadcrumbs(directory = currentDirectory) {
            viewModel.navigateToDirectory(it)
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)

        if (currentDirectory == FileSystemItem.Directory.nullDirectory) {
            Spacer(modifier = Modifier.weight(1f))
            NoFilesFound(
                onCancel = onCancel
            )
        } else {
            FilePickerGrid(
                visibleItems = visibleItems,
                onFileChosen = onFileChosen,
                viewModel = viewModel
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PaginationControls(viewModel)
    }
}

@Composable
fun Header() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = "Choose a file",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun NoFilesFound(
    onCancel: () -> Unit
) {
    Column {
        Text(
            text = "You have no drawing files!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onCancel() },
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
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

@Composable
fun FilePickerGrid(
    visibleItems: List<FileSystemItem>,
    onFileChosen: (String) -> Unit,
    viewModel: PaintFileBrowserViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(visibleItems) { item ->
            when (item) {
                is FileSystemItem.File -> {
                    FileItem(item, onClick = { onFileChosen(it.path) })
                }
                is FileSystemItem.Directory -> {
                    DirectoryItem(item, onClick = { viewModel.navigateToDirectory(it) })
                }
            }
        }
    }
}

@Composable
fun DirectoryItem(directory: FileSystemItem.Directory, onClick: (FileSystemItem.Directory) -> Unit) {
    GridItem(
        file = directory,
        onClick = onClick,
        image = {
            Image(
                painter = painterResource(id = R.drawable.folder),
                contentDescription = "Directory"
            )
        },
    )
}

@Composable
fun FileItem(
    file: FileSystemItem.File,
    onClick: (FileSystemItem.File) -> Unit
) {
    GridItem(
        file = file,
        onClick = onClick,
        image = {
            LazyPaintImage(
                filePath = file.path,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = 1.dp, color = Color.Black)
            )
        },
    )
}

@Composable
fun <T : FileSystemItem> GridItem(
    file: T,
    image: @Composable () -> Unit,
    onClick: (T) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(file) }
    ) {
        image()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = file.name,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = file.lengthString,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun Breadcrumbs(
    directory: FileSystemItem.Directory,
    navigateToDirectory: (FileSystemItem.Directory) -> Unit
) {
    val breadcrumbs = directory.breadcrumbs.toList().reversed()
    val style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        breadcrumbs.forEachIndexed { index, dir ->
            Text(
                text = dir.name,
                modifier = Modifier.clickable {
                    navigateToDirectory(dir)
                },
                style = style
            )
            if (index < breadcrumbs.lastIndex) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(">>", style = style)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun PaginationControls(
    viewModel: PaintFileBrowserViewModel,
) {
    val currentPageIndex: Int by viewModel.currentPageIndex.collectAsState()
    val numPagesTotal: Int by viewModel.numPagesTotal.collectAsState()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { viewModel.loadPreviousPage() },
            enabled = currentPageIndex > 1
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
        }
        Text(
            text = "$currentPageIndex / $numPagesTotal",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(
            onClick = { viewModel.loadNextPage() },
            enabled = currentPageIndex < numPagesTotal
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
        }
    }
}
