

package com.duckduckgo.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.duckduckgo.mobile.android.R

class SkeletonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_DuckDuckGo_SkeletonView,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.SkeletonView,
            0,
            R.style.Widget_DuckDuckGo_SkeletonView,
        ).apply {
            background = getDrawable(R.styleable.SkeletonView_android_background)
            recycle()
        }
    }
}
