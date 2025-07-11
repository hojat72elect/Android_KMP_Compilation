

package com.duckduckgo.sync.impl.ui.qrcode

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.duckduckgo.common.ui.view.gone
import com.duckduckgo.common.ui.view.show
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.sync.impl.R
import com.duckduckgo.sync.impl.databinding.ViewBlurredCtaBinding

class BlurredTextContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewBlurredCtaBinding by viewBinding()

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.BlurredTextContainer,
            0,
            0,
        ).apply {
            setPrimaryText(getString(R.styleable.BlurredTextContainer_primaryText))
            val showTrailingIcon = hasValue(R.styleable.BlurredTextContainer_trailingIcon)
            if (showTrailingIcon) {
                setTrailingIconDrawable(getDrawable(R.styleable.BlurredTextContainer_trailingIcon)!!)
                binding.trailingIconContainer.show()
            } else {
                binding.trailingIconContainer.gone()
            }
            recycle()
        }
    }

    /** Sets the primary text title */
    fun setPrimaryText(title: String?) {
        binding.primaryText.text = title
    }

    /** Sets the trailing icon image drawable */
    fun setTrailingIconDrawable(drawable: Drawable) {
        binding.trailingIcon.setImageDrawable(drawable)
    }
}
