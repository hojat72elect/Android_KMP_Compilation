

package com.duckduckgo.savedsites.impl.bookmarks

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.duckduckgo.saved.sites.impl.databinding.BottomSheetFaviconsPromptBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("NoBottomSheetDialog")
class FaviconPromptSheet(
    builder: Builder,
) : BottomSheetDialog(builder.context) {

    private val binding = BottomSheetFaviconsPromptBinding.inflate(LayoutInflater.from(context))
    internal class DefaultEventListener : EventListener()

    abstract class EventListener {
        open fun onFaviconsFetchingPromptDismissed(fetchingEnabled: Boolean = false) {}
    }

    init {
        setContentView(binding.root)
        binding.faviconsPromptPrimaryCta.setOnClickListener {
            builder.listener.onFaviconsFetchingPromptDismissed(true)
            dismiss()
        }
        binding.faviconsPromptSecondaryCta.setOnClickListener {
            builder.listener.onFaviconsFetchingPromptDismissed()
            dismiss()
        }
        setOnCancelListener {
            builder.listener.onFaviconsFetchingPromptDismissed()
        }
    }

    /**
     * Creates a builder for an action bottom sheet dialog that uses
     * the default bottom sheet dialog theme.
     *
     * @param context the parent context
     */
    class Builder(val context: Context) {
        var dialog: BottomSheetDialog? = null
        var listener: EventListener = DefaultEventListener()

        fun addEventListener(eventListener: EventListener): Builder {
            listener = eventListener
            return this
        }

        fun show() {
            dialog = FaviconPromptSheet(this)
            dialog?.show()
        }
    }
}
