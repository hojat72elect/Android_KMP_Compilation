

package com.duckduckgo.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.mobile.android.R
import com.duckduckgo.mobile.android.databinding.ViewSearchBarBinding

interface SearchBar {
    fun onAction(actionHandler: (Action) -> Unit)
    fun handle(event: Event)

    enum class Event {
        DismissSearchBar,
        ShowSearchBar,
    }

    sealed class Action {
        object PerformUpAction : Action()
        data class PerformSearch(val searchText: String) : Action()
    }
}

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_DuckDuckGo_SearchBarView,
) : ConstraintLayout(context, attrs, defStyleAttr), SearchBar {
    private val binding: ViewSearchBarBinding by viewBinding()

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.SearchBarView,
            0,
            R.style.Widget_DuckDuckGo_SearchBarView,
        ).apply {
            binding.root.background = getDrawable(R.styleable.SearchBarView_android_background)
            binding.omnibarTextInput.hint = getString(R.styleable.SearchBarView_searchHint)
            binding.clearTextButton.contentDescription = getString(R.styleable.SearchBarView_clearActionContentDescription)
            binding.upAction.contentDescription = getString(R.styleable.SearchBarView_upActionContentDescription)
            recycle()
        }
    }

    override fun onAction(actionHandler: (SearchBar.Action) -> Unit) {
        binding.upAction.setOnClickListener {
            actionHandler(SearchBar.Action.PerformUpAction)
        }
        binding.omnibarTextInput.doOnTextChanged { text, _, _, _ ->
            binding.clearTextButton.visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
            actionHandler(SearchBar.Action.PerformSearch(text.toString()))
        }
        binding.clearTextButton.setOnClickListener {
            clearText()
        }
    }

    override fun handle(event: SearchBar.Event) {
        when (event) {
            SearchBar.Event.DismissSearchBar -> {
                clearText()
                binding.root.visibility = GONE
            }
            SearchBar.Event.ShowSearchBar -> {
                clearText()
                binding.omnibarTextInput.showKeyboard()
                binding.root.visibility = VISIBLE
            }
        }
    }

    private fun clearText() {
        binding.omnibarTextInput.text?.clear()
    }
}
