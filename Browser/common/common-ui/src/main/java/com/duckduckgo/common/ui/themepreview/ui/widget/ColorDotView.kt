package com.duckduckgo.common.ui.themepreview.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.duckduckgo.mobile.android.R

/** Simple view that draws a filled circle with a stroke. */
class ColorDotView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    View(context, attrs, defStyleAttr) {

    var fillColor: Int = Color.LTGRAY
        set(value) {
            paintFill.color = value
            field = value
        }

    var strokeColor: Int = Color.DKGRAY
        set(value) {
            paintStroke.color = value
            field = value
        }

    private val paintFill =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.RED
        }

    private val paintStroke =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
        }

    private var cx: Float = 0F
    private var cy: Float = 0F
    private var radius: Float = 0F

    init {
        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ColorDotView, defStyleAttr, 0)
        fillColor = a.getColor(R.styleable.ColorDotView_colorFillColor, fillColor)
        strokeColor = a.getColor(R.styleable.ColorDotView_colorStrokeColor, strokeColor)
        a.recycle()
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int,
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2F
        cy = h / 2F
        // Decreases our circle's radius slightly so our stroke doesn't get clipped.
        radius = (w / 2F) - 1F
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(cx, cy, radius, paintFill)
        canvas.drawCircle(cx, cy, radius, paintStroke)
    }
}
