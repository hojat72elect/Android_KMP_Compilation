

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.databinding.ViewDeviceShieldPastWeekActivityContentBinding

class PastWeekTrackerActivityContentView : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : this(context, attrs, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int,
    ) : super(context, attrs, defStyle) {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PastWeekTrackerActivityContentView)
        text = attributes.getString(R.styleable.PastWeekTrackerActivityContentView_android_text) ?: ""
        count = attributes.getString(R.styleable.PastWeekTrackerActivityContentView_count) ?: ""
        footer = attributes.getString(R.styleable.PastWeekTrackerActivityContentView_footer) ?: ""
        attributes.recycle()
    }

    private val binding: ViewDeviceShieldPastWeekActivityContentBinding by viewBinding()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.root.isVisible = true
    }

    var count: String
        get() {
            return binding.contentText.text.toString()
        }
        set(value) {
            binding.contentText.text = value
        }

    var text: String
        get() {
            return binding.contentTitle.text.toString()
        }
        set(value) {
            binding.contentTitle.text = value
        }

    var footer: String
        get() {
            return binding.contentFooter.text.toString()
        }
        set(value) {
            binding.contentFooter.text = value
        }
}
