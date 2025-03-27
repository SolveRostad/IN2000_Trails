package no.uio.ifi.in2000_gruppe3.ui.loaders

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun HikeManLoader() {
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context)
        .components {
            if ( SDK_INT >= 28 ) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.7f)
            )
            .padding(top = 300.dp)
            .zIndex(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        AsyncImage(
            imageLoader = gifEnabledLoader,
            model = R.drawable.hiking_loader,
            contentDescription = "Loading",
            modifier = Modifier
                .size(150.dp)
                .fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.White)
        )
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
        )
    }



}

@Preview
@Composable
fun LoaderPreview() {
    HikeManLoader()
}