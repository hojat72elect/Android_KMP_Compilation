

package com.duckduckgo.common.ui.themepreview.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.duckduckgo.mobile.android.R

/** Composite view to show an item containing a text label and a [ColorDotView]. */
class ColorAttributeView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val colorAttributeTextView: AppCompatTextView
    private val colorDotView: ColorDotView

    private var attributeText: String = ""
        set(value) {
            colorAttributeTextView.text = value
            field = value
        }

    private var dotFillColor: Int = Color.LTGRAY
        set(value) {
            colorDotView.fillColor = value
            field = value
        }

    private var dotStrokeColor: Int = Color.DKGRAY
        set(value) {
            colorDotView.strokeColor = value
            field = value
        }

    init {
        val view = View.inflate(context, R.layout.view_layout_color_attribute, this)
        colorAttributeTextView = view.findViewById(R.id.color_attribute)
        colorDotView = view.findViewById(R.id.color_dot)

        val a =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ColorAttributeView,
                defStyleAttr,
                defStyleRes,
            )
        attributeText = a.getString(R.styleable.ColorAttributeView_android_text) ?: attributeText
        dotFillColor = a.getColor(R.styleable.ColorAttributeView_colorFillColor, dotFillColor)
        dotStrokeColor = a.getColor(R.styleable.ColorAttributeView_colorStrokeColor, dotStrokeColor)
        a.recycle()
    }
}
