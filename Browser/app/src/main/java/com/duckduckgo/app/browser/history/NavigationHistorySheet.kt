

package com.duckduckgo.app.browser.history

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.browser.commands.Command.ShowBackNavigationHistory
import com.duckduckgo.app.browser.databinding.NavigationHistoryPopupViewBinding
import com.duckduckgo.app.browser.favicon.FaviconManager
import com.duckduckgo.app.browser.history.NavigationHistoryAdapter.NavigationHistoryListener
import com.google.android.material.bottomsheet.BottomSheetDialog

@SuppressLint("NoBottomSheetDialog")
class NavigationHistorySheet(
    context: Context,
    private val viewLifecycleOwner: LifecycleOwner,
    private val faviconManager: FaviconManager,
    private val tabId: String,
    private val history: ShowBackNavigationHistory,
    private val listener: NavigationHistorySheetListener,
) : BottomSheetDialog(context) {

    private val binding = NavigationHistoryPopupViewBinding.inflate(LayoutInflater.from(context))

    interface NavigationHistorySheetListener {
        fun historicalPageSelected(stackIndex: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.historyRecycler.also { recycler ->
            NavigationHistoryAdapter(
                viewLifecycleOwner,
                faviconManager,
                tabId,
                object : NavigationHistoryListener {
                    override fun historicalPageSelected(stackIndex: Int) {
                        dismiss()
                        listener.historicalPageSelected(stackIndex)
                    }
                },
            ).also { adapter ->
                recycler.adapter = adapter
                adapter.updateNavigationHistory(history.history)
            }
        }
    }
}
