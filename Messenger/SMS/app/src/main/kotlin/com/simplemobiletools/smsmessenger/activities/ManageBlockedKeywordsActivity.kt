package com.simplemobiletools.smsmessenger.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.simplemobiletools.smsmessenger.helpers.NavigationIcon
import com.simplemobiletools.smsmessenger.interfaces.RefreshRecyclerViewListener
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.databinding.ActivityManageBlockedKeywordsBinding
import com.simplemobiletools.smsmessenger.dialogs.AddBlockedKeywordDialog
import com.simplemobiletools.smsmessenger.dialogs.ManageBlockedKeywordsAdapter
import com.simplemobiletools.smsmessenger.extensions.beVisibleIf
import com.simplemobiletools.smsmessenger.extensions.config
import com.simplemobiletools.smsmessenger.extensions.getProperPrimaryColor
import com.simplemobiletools.smsmessenger.extensions.toArrayList
import com.simplemobiletools.smsmessenger.extensions.underlineText
import com.simplemobiletools.smsmessenger.extensions.updateTextColors
import com.simplemobiletools.smsmessenger.extensions.viewBinding
import com.simplemobiletools.smsmessenger.helpers.APP_ICON_IDS
import com.simplemobiletools.smsmessenger.helpers.APP_LAUNCHER_NAME
import com.simplemobiletools.smsmessenger.helpers.ensureBackgroundThread

@RequiresApi(Build.VERSION_CODES.O)
class ManageBlockedKeywordsActivity : BaseSimpleActivity(), RefreshRecyclerViewListener {
    override fun getAppIconIDs() = intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList()

    override fun getAppLauncherName() = intent.getStringExtra(APP_LAUNCHER_NAME) ?: ""

    private val binding by viewBinding(ActivityManageBlockedKeywordsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateBlockedKeywords()
        setupOptionsMenu()

        updateMaterialActivityViews(
            mainCoordinatorLayout = binding.blockKeywordsCoordinator,
            nestedView = binding.manageBlockedKeywordsList,
            useTransparentNavigation = true,
            useTopSearchMenu = false
        )
        setupMaterialScrollListener(
            scrollingView = binding.manageBlockedKeywordsList,
            toolbar = binding.blockKeywordsToolbar
        )
        updateTextColors(binding.manageBlockedKeywordsWrapper)

        binding.manageBlockedKeywordsPlaceholder2.apply {
            underlineText()
            setTextColor(getProperPrimaryColor())
            setOnClickListener {
                addOrEditBlockedKeyword()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.blockKeywordsToolbar, NavigationIcon.Arrow)
    }

    private fun setupOptionsMenu() {
        binding.blockKeywordsToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_blocked_keyword -> {
                    addOrEditBlockedKeyword()
                    true
                }

                else -> false
            }
        }
    }

    override fun refreshItems() {
        updateBlockedKeywords()
    }

    private fun updateBlockedKeywords() {
        ensureBackgroundThread {
            val blockedKeywords = config.blockedKeywords
            runOnUiThread {
                ManageBlockedKeywordsAdapter(
                    this,
                    blockedKeywords.toArrayList(),
                    this,
                    binding.manageBlockedKeywordsList
                ) {
                    addOrEditBlockedKeyword(it as String)
                }.apply {
                    binding.manageBlockedKeywordsList.adapter = this
                }

                binding.manageBlockedKeywordsPlaceholder.beVisibleIf(blockedKeywords.isEmpty())
                binding.manageBlockedKeywordsPlaceholder2.beVisibleIf(blockedKeywords.isEmpty())
            }
        }
    }

    private fun addOrEditBlockedKeyword(keyword: String? = null) {
        AddBlockedKeywordDialog(this, keyword) {
            updateBlockedKeywords()
        }
    }
}
