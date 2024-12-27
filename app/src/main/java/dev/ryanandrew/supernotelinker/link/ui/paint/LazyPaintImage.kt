package dev.ryanandrew.supernotelinker.link.ui.paint

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.asImage
import coil3.compose.SubcomposeAsyncImage
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.fetch.SourceFetchResult
import coil3.request.ImageRequest
import coil3.request.Options
import dev.ryanandrew.supernotelinker.link.PaintHelper

@Composable
fun LazyPaintImage(
    filePath: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                add(PaintFileDecoder.Factory(PaintHelper(context)))
            }
            .build()
    }
    val placeholder = remember {
        BitmapPainter(placeholderBitmap.asImageBitmap())
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(filePath)
            .build(),
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        loading = {
            Image(
                painter = placeholder,
                contentDescription = contentDescription
            )
        }
    )
}

private class PaintFileDecoder(
    private val path: String,
    private val paintHelper: PaintHelper
) : Decoder {
    override suspend fun decode(): DecodeResult {
        val image = paintHelper.getPaintThumbnail(path) ?: placeholderBitmap
        return DecodeResult(
            image = image.asImage(),
            isSampled = true
        )
    }

    class Factory(private val paintHelper: PaintHelper) : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder {
            return PaintFileDecoder(result.source.file().toString(), paintHelper)
        }
    }
}

val placeholderBitmap: Bitmap by lazy { Bitmap.createBitmap(600, 800, Bitmap.Config.ARGB_8888) }
