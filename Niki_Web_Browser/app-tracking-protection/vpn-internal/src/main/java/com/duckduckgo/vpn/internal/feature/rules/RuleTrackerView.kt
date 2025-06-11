

package com.duckduckgo.vpn.internal.feature.rules

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.vpn.internal.databinding.ExceptionRuleDomainViewBinding

class RuleTrackerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ExceptionRuleDomainViewBinding by viewBinding()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.checkBox.setOnCheckedChangeListener { _, isEnable ->
            ruleTrackerListener?.onTrackerClicked(this, isEnable)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.root.setOnClickListener(null)
    }

    var ruleTrackerListener: RuleTrackerListener? = null

    var domain: String
        get() = binding.trackerDomain.toString()
        set(value) {
            binding.trackerDomain.text = value
        }

    var isChecked: Boolean
        get() = binding.checkBox.isChecked
        set(value) {
            binding.checkBox.isChecked = value
        }

    interface RuleTrackerListener {
        fun onTrackerClicked(
            view: View,
            enabled: Boolean,
        )
    }
}
