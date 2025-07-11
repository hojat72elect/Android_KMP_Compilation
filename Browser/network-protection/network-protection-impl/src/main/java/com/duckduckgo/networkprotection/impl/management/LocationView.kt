

package com.duckduckgo.networkprotection.impl.management

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.duckduckgo.common.ui.view.gone
import com.duckduckgo.common.ui.view.listitem.DaxListItem.ImageBackground
import com.duckduckgo.common.ui.view.show
import com.duckduckgo.common.ui.view.text.DaxTextView
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.networkprotection.impl.databinding.ViewLocationBinding

class LocationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLocationBinding by viewBinding()

    val details: DaxTextView
        get() = binding.locationViewDetails
    val status: DaxTextView
        get() = binding.locationViewStatus

    fun setClickListener(onClick: () -> Unit) {
        binding.itemContainer.setOnClickListener { onClick() }
    }

    /** Sets the primary text title */
    fun setDetails(title: String?) {
        binding.locationViewDetails.text = (title)
    }

    /** Sets the secondary text title */
    fun setStatus(title: String?) {
        binding.locationViewStatus.text = title
    }

    /** Sets the leading icon using emoji*/
    fun setLeadingIcon(emoji: String) {
        binding.locationViewIcon.gone()
        binding.locationViewEmojiIcon.show()
        binding.locationViewEmojiBackground.setBackgroundResource(ImageBackground.background(ImageBackground.from(1)))
        binding.locationViewEmojiIcon.text = emoji
    }

    /** Sets the leading icon using drawable res */
    fun setLeadingIcon(@DrawableRes icon: Int) {
        binding.locationViewEmojiIcon.gone()
        binding.locationViewIcon.show()
        binding.locationViewEmojiBackground.setBackgroundResource(ImageBackground.background(ImageBackground.from(1)))
        binding.locationViewIcon.setImageResource(icon)
    }
}
