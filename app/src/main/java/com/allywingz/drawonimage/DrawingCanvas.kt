package com.allywingz.drawonimage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.graphics.createBitmap

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    vm: DrawingCanvasViewModel
) {
    val bitmap = remember {
        createBitmap(1080, 1920)
    }

    val canvas = android.graphics.Canvas(bitmap)

    Canvas(
        modifier = modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { vm.onTapGesture(it) }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { vm.onDragStart(it) },
                    onDragEnd = { vm.onDragEnd() },
                    onDragCancel = { vm.onDragCancel() },
                    onDrag = { _, amount -> vm.onDrag(amount) }
                )
            }
    ) {
        vm.paths.forEach { (path, color) ->
            drawPath(path = path, color = color, style = Stroke(10f))
            val paint = android.graphics.Paint().apply {
                this.color = color.toArgb()
                this.style = android.graphics.Paint.Style.STROKE
                this.strokeWidth = 10f
            }
            canvas.drawPath(path.asAndroidPath(), paint)
        }

        vm.currentPath.value?.let { path ->
            drawPath(path = path, color = vm.currentColor.value, style = Stroke(10f))
            val paint = android.graphics.Paint().apply {
                this.color = vm.currentColor.value.toArgb()
                this.style = android.graphics.Paint.Style.STROKE
                this.strokeWidth = 10f
            }
            canvas.drawPath(path.asAndroidPath(), paint)
        }
        vm.setCanvasBitmap(bitmap)
    }
}



