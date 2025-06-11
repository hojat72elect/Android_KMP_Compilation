

package com.duckduckgo.vpn.internal.feature.rules

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.vpn.internal.databinding.ExceptionRuleAppViewBinding

class RuleAppView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ExceptionRuleAppViewBinding by viewBinding()

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.root.setOnClickListener(null)
    }

    var appName: String
        get() = binding.ruleAppName.toString()
        set(value) {
            binding.ruleAppName.text = value
        }

    var appIcon: Drawable?
        get() = binding.ruleAppIcon.drawable
        set(value) {
            binding.ruleAppIcon.setImageDrawable(value)
        }

    fun removeAllTrackerViews() {
        binding.appDomainRules.removeAllViews()
    }

    fun addTrackerView(view: View) {
        binding.appDomainRules.addView(view)
    }
}
