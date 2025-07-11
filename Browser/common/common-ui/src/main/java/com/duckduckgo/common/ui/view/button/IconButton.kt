

package com.duckduckgo.common.ui.view.button

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.duckduckgo.mobile.android.R

class IconButton
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_DuckDuckGo_IconButton,
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    init {
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.IconButton,
                0,
                R.style.Widget_DuckDuckGo_IconButton,
            )

        val resourceId = typedArray.getResourceId(
            R.styleable.IconButton_srcCompat,
            R.drawable.ic_union,
        )

        setImageResource(resourceId)
        setBackgroundDrawable(typedArray.getDrawable(R.styleable.IconButton_android_background))

        typedArray.recycle()
    }
}
