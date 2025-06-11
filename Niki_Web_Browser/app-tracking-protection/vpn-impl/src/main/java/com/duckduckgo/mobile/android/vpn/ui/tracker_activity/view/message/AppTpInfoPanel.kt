

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.mobile.android.vpn.databinding.ViewMessageInfoDisabledBinding
import com.duckduckgo.mobile.android.vpn.databinding.ViewMessageInfoEnabledBinding

class AppTpEnabledInfoPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMessageInfoEnabledBinding by viewBinding()

    fun setClickableLink(
        annotation: String,
        fullText: CharSequence,
        onClick: () -> Unit,
    ) {
        binding.root.setClickableLink(annotation, fullText, onClick)
    }
}

class AppTpDisabledInfoPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMessageInfoDisabledBinding by viewBinding()

    fun setClickableLink(
        annotation: String,
        fullText: CharSequence,
        onClick: () -> Unit,
    ) {
        binding.root.setClickableLink(annotation, fullText, onClick)
    }
}
