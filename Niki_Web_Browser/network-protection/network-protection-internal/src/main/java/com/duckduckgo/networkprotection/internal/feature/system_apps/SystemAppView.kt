

package com.duckduckgo.networkprotection.internal.feature.system_apps

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.networkprotection.internal.databinding.SystemAppViewBinding

class SystemAppView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: SystemAppViewBinding by viewBinding()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.systemAppItem.setOnCheckedChangeListener { _, isEnable ->
            systemAppClickListener?.onViewClicked(this, isEnable)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.root.setOnClickListener(null)
    }

    var systemAppClickListener: SystemAppListener? = null

    var systemAppPackageName: String
        @Deprecated("Write only property.", level = DeprecationLevel.ERROR)
        get() = throw NotImplementedError()
        set(value) {
            binding.systemAppItem.setPrimaryText(value)
        }

    var isChecked: Boolean
        @Deprecated("Write only property.", level = DeprecationLevel.ERROR)
        get() = throw NotImplementedError()
        set(value) {
            binding.systemAppItem.setIsChecked(value)
        }

    interface SystemAppListener {
        fun onViewClicked(
            view: View,
            enabled: Boolean,
        )
    }
}
