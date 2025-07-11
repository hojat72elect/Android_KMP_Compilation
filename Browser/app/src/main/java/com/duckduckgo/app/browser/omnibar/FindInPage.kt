

package com.duckduckgo.app.browser.omnibar

import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.duckduckgo.app.browser.databinding.IncludeFadeOmnibarFindInPageBinding
import com.duckduckgo.app.browser.databinding.IncludeFindInPageBinding
import com.duckduckgo.common.ui.view.text.DaxTextView

/**
 * Compatibility interface for accessing [IncludeFindInPageBinding] or [IncludeFadeOmnibarFindInPageBinding], depending on which omnibar is used.
 */
interface FindInPage {
    val findInPageContainer: ViewGroup
    val findInPageInput: EditText
    val findInPageMatches: DaxTextView
    val previousSearchTermButton: ImageView
    val nextSearchTermButton: ImageView
    val closeFindInPagePanel: ImageView
}

class FindInPageImpl(
    override val findInPageContainer: ViewGroup,
    override val findInPageInput: EditText,
    override val findInPageMatches: DaxTextView,
    override val previousSearchTermButton: ImageView,
    override val nextSearchTermButton: ImageView,
    override val closeFindInPagePanel: ImageView,
) : FindInPage {

    constructor(binding: IncludeFindInPageBinding) : this(
        findInPageContainer = binding.findInPageContainer,
        findInPageInput = binding.findInPageInput,
        findInPageMatches = binding.findInPageMatches,
        previousSearchTermButton = binding.previousSearchTermButton,
        nextSearchTermButton = binding.nextSearchTermButton,
        closeFindInPagePanel = binding.closeFindInPagePanel,
    )

    constructor(binding: IncludeFadeOmnibarFindInPageBinding) : this(
        findInPageContainer = binding.findInPageContainer,
        findInPageInput = binding.findInPageInput,
        findInPageMatches = binding.findInPageMatches,
        previousSearchTermButton = binding.previousSearchTermButton,
        nextSearchTermButton = binding.nextSearchTermButton,
        closeFindInPagePanel = binding.closeFindInPagePanel,
    )
}
