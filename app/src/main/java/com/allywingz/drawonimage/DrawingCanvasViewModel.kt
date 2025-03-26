package com.allywingz.drawonimage

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale

class DrawingCanvasViewModel : ViewModel() {
    private val drawingPath = Path()
    val paths = mutableStateListOf<Pair<Path, Color>>()
    val currentPath = mutableStateOf<Path?>(null)
    val currentColor = mutableStateOf<Color>(Color.Black)
    private var bitmap: Bitmap? = null
    private val lastOffset = mutableStateOf<Offset?>(null)

    private val pointList = mutableListOf<Offset>()

    fun getCanvasBitmap(): Bitmap? {
        return bitmap
    }
    fun setCanvasBitmap(newBitmap: Bitmap) {
        bitmap = newBitmap
    }

    fun clear() {
        paths.clear()
        currentPath.value = null
        lastOffset.value = null
        bitmap = null
        drawingPath.reset()
    }

    fun onTapGesture(offset: Offset) {
        currentPath.value = Path().apply {
            moveTo(offset.x, offset.y)
            addRect(Rect(offset.x - 0.5f, offset.y - 0.5f, offset.x + 0.5f, offset.y + 0.5f))
        }
        currentPath.value?.let { path ->
            paths.add(Pair(path, currentColor.value))
        }
    }

    fun onDragStart(offset: Offset) {
        drawingPath.reset()
        drawingPath.moveTo(offset.x, offset.y)
        pointList.clear()
        pointList.add(offset)
        currentPath.value = drawingPath
    }

    fun onDragEnd() {
        currentPath.value?.let { path ->
            paths.add(Pair(Path().apply { addPath(path) }, currentColor.value))
        }
        currentPath.value = null
        drawingPath.reset()
        pointList.clear()
    }

    fun onDragCancel() {
        currentPath.value = null
    }

    fun onDrag(offset: Offset) {
        val newOffset = offset
        pointList.add(newOffset)

        if (pointList.size >= 3) {
            val current = pointList[pointList.size - 2]
            val next = pointList.last()

            val control = current
            val endPoint = Offset((current.x + next.x) / 2, (current.y + next.y) / 2)

            drawingPath.quadraticTo(control.x, control.y, endPoint.x, endPoint.y)
        }

        currentPath.value = drawingPath
        lastOffset.value = newOffset

        currentPath.value = Path().apply { addPath(drawingPath) }
    }

    fun setDrawingColor(color: Color) {
        currentColor.value = color
    }

    fun overlayBitmaps(baseBitmap: Bitmap, overlayBitmap: Bitmap?): Bitmap {
        val width = baseBitmap.width
        val height = baseBitmap.height

        val resultBitmap = createBitmap(width, height, baseBitmap.config!!)
        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(baseBitmap, 0f, 0f, null)

        val paint = Paint().apply {
            alpha = 255 // Set the transparency (0-255)
        }
        canvas.drawBitmap(overlayBitmap!!, 0f, 0f, paint)

        return resultBitmap
    }

    fun resizeBitmap(originalBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return originalBitmap.scale(newWidth, newHeight)
    }
}

