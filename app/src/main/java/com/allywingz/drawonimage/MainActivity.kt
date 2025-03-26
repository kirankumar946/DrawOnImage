package com.allywingz.drawonimage

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.allywingz.drawonimage.ui.theme.DrawOnImageTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DrawOnImageTheme {
                ImageWithDialogExample()
            }
        }
    }
}

@Composable
fun ImageWithDialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    var switchState by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var drawnImage by remember { mutableStateOf<Bitmap?>(null) }
    var composableSize by remember { mutableStateOf(IntSize.Zero) }

    val drawingColor = remember { mutableStateOf(Color.Black) }
    val drawingCanvasViewModel = remember { DrawingCanvasViewModel() }

    val imageUrl = "https://fastly.picsum.photos/id/12/536/354.jpg?hmac=tSKhIIVeHahhRtQ8w9tZUA_E0yh38t7Ur43wbjkaatg"
    val context = LocalContext.current
    LaunchedEffect(imageUrl) {
        val imageLoader = ImageLoader.Builder(context).build()
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()
        val result = imageLoader.execute(request)
        if (result.drawable != null) {
            val drawable = result.drawable!!
            drawnImage = drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showDialog) {
            switchState = false
            drawingCanvasViewModel.clear() // Clear previous drawings
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(250.dp)
                                .clip(RectangleShape)
                                .onGloballyPositioned { coordinates ->
                                    composableSize = coordinates.size
                                }
                        ) {
                            AsyncImage(
                                model = "https://fastly.picsum.photos/id/12/536/354.jpg?hmac=tSKhIIVeHahhRtQ8w9tZUA_E0yh38t7Ur43wbjkaatg",
                                contentDescription = "Loaded Image",
                                modifier = Modifier.size(250.dp),
                                contentScale = ContentScale.Fit
                            )
                            DrawingCanvas(
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(RectangleShape),
                                vm = drawingCanvasViewModel
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Switch Drawing Color: ")
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = switchState,
                                onCheckedChange = { newState ->
                                    switchState = newState
                                    drawingColor.value = if (newState) Color.Red else Color.Black
                                    drawingCanvasViewModel.setDrawingColor(drawingColor.value)
                                }
                            )
                        }
                        Button(onClick = {
                            showDialog = false
                            drawnImage?.let { imageBitmap ->
                                val savedCanvas = drawingCanvasViewModel.getCanvasBitmap()
                                val resizedBitmap = drawingCanvasViewModel.resizeBitmap(
                                    imageBitmap,
                                    composableSize.width,
                                    composableSize.height
                                )
                                val combinedBitmap = drawingCanvasViewModel.overlayBitmaps(
                                    resizedBitmap,
                                    savedCanvas
                                )
                                capturedBitmap = combinedBitmap.asImageBitmap()
                            }
                        }) {
                            Text("Save Drawing")
                        }
                    }
                }
            }
        }

        capturedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = "Captured Composable",
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 16.dp),
                contentScale = ContentScale.Fit
            )
        } ?: run {
            AsyncImage(
                model = "https://fastly.picsum.photos/id/12/536/354.jpg?hmac=tSKhIIVeHahhRtQ8w9tZUA_E0yh38t7Ur43wbjkaatg",
                contentDescription = "Loaded Image",
                modifier = Modifier
                    .size(200.dp)
                    .clickable { showDialog = true },
                contentScale = ContentScale.Crop
            )
        }
    }
}
