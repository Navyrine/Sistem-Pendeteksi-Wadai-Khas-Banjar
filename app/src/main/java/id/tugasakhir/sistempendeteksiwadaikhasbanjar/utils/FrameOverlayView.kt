package id.tugasakhir.sistempendeteksiwadaikhasbanjar.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import id.tugasakhir.sistempendeteksiwadaikhasbanjar.R

class FrameOverlayView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attr, defStyleAttr) {
    private val frameRect = Rect()
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        color = ContextCompat.getColor(context, R.color.md_theme_primaryContainer)
    }
    private val eraserPaint = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val blurPaint = Paint().apply {
        color = Color.argb(150, 0, 0, 0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = (width * FRAME_LEFT_RATIO).toInt()
        val top = (height * FRAME_TOP_RATIO).toInt()
        val right = (width * FRAME_RIGHT_RATIO).toInt()
        val bottom = (height * FRAME_BOTTOM_RATIO).toInt()
        frameRect.set(left, top, right, bottom)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), blurPaint)
        canvas.drawRect(frameRect, eraserPaint)
        canvas.drawRect(frameRect, paint)
    }

    fun getFrameRect(): Rect {
        return frameRect
    }

    companion object {
        const val FRAME_LEFT_RATIO = 0.1f
        const val FRAME_TOP_RATIO = 0.3f
        const val FRAME_RIGHT_RATIO = 0.9f
        const val FRAME_BOTTOM_RATIO = 0.7f
    }
}