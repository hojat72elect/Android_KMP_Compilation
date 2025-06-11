

package com.duckduckgo.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.mobile.android.R
import com.duckduckgo.mobile.android.databinding.ViewStatusIndicatorBinding

class StatusIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewStatusIndicatorBinding by viewBinding()

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.StatusIndicator,
            0,
            0,
        ).apply {

            val status = Status.from(getInt(R.styleable.StatusIndicator_indicatorStatus, 0))
            setStatus(status)

            recycle()
        }
    }

    fun setStatus(status: Status) {
        when (status) {
            Status.ALWAYS_ON -> {
                binding.icon.isEnabled = true
                binding.label.text = context.getString(R.string.alwaysOn)
            }
            Status.ON -> {
                binding.icon.isEnabled = true
                binding.label.text = context.getString(R.string.on)
            }
            Status.OFF -> {
                binding.icon.isEnabled = false
                binding.label.text = context.getString(R.string.off)
            }
        }
    }

    fun setStatus(isOn: Boolean) {
        setStatus(if (isOn) Status.ON else Status.OFF)
    }

    enum class Status {

        ALWAYS_ON,
        ON,
        OFF,
        ;

        companion object {

            fun from(value: Int): Status = when (value) {
                0 -> ALWAYS_ON
                1 -> ON
                2 -> OFF
                else -> OFF
            }
        }
    }
}
