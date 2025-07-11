package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.biometric.auth.AuthPromptHost
import androidx.fragment.app.FragmentActivity
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogSecurityBinding
import ca.hojat.smart.musicplayer.shared.data.HashListener
import ca.hojat.smart.musicplayer.shared.extensions.baseConfig
import ca.hojat.smart.musicplayer.shared.extensions.beGone
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.getProperBackgroundColor
import ca.hojat.smart.musicplayer.shared.extensions.getProperPrimaryColor
import ca.hojat.smart.musicplayer.shared.extensions.getProperTextColor
import ca.hojat.smart.musicplayer.shared.extensions.isBiometricIdAvailable
import ca.hojat.smart.musicplayer.shared.extensions.onGlobalLayout
import ca.hojat.smart.musicplayer.shared.extensions.onPageChangeListener
import ca.hojat.smart.musicplayer.shared.extensions.onTabSelectionChanged
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.helpers.PROTECTION_FINGERPRINT
import ca.hojat.smart.musicplayer.shared.helpers.PROTECTION_PATTERN
import ca.hojat.smart.musicplayer.shared.helpers.PROTECTION_PIN
import ca.hojat.smart.musicplayer.shared.helpers.SHOW_ALL_TABS
import ca.hojat.smart.musicplayer.shared.ui.views.MyDialogViewPager
import ca.hojat.smart.musicplayer.shared.ui.views.PasswordTypesAdapter

class SecurityDialog(
    private val activity: Activity,
    private val requiredHash: String,
    private val showTabIndex: Int,
    private val callback: (hash: String, type: Int, success: Boolean) -> Unit
) : HashListener {
    private var dialog: AlertDialog? = null
    private val view = DialogSecurityBinding.inflate(LayoutInflater.from(activity), null, false)
    private var tabsAdapter: PasswordTypesAdapter
    private var viewPager: MyDialogViewPager

    init {
        view.apply {
            viewPager = dialogTabViewPager
            viewPager.offscreenPageLimit = 2
            tabsAdapter = PasswordTypesAdapter(
                context = root.context,
                requiredHash = requiredHash,
                hashListener = this@SecurityDialog,
                scrollView = dialogScrollview,
                biometricPromptHost = AuthPromptHost(activity as FragmentActivity),
                showBiometricIdTab = shouldShowBiometricIdTab(),
                showBiometricAuthentication = showTabIndex == PROTECTION_FINGERPRINT
            )
            viewPager.adapter = tabsAdapter
            viewPager.onPageChangeListener {
                dialogTabLayout.getTabAt(it)?.select()
            }

            viewPager.onGlobalLayout {
                updateTabVisibility()
            }

            if (showTabIndex == SHOW_ALL_TABS) {
                val textColor = root.context.getProperTextColor()

                if (shouldShowBiometricIdTab()) {
                    dialogTabLayout.addTab(
                        dialogTabLayout.newTab().setText(R.string.biometrics),
                        PROTECTION_FINGERPRINT
                    )
                }

                if (activity.baseConfig.isUsingSystemTheme) {
                    dialogTabLayout.setBackgroundColor(activity.resources.getColor(R.color.you_dialog_background_color))
                } else {
                    dialogTabLayout.setBackgroundColor(root.context.getProperBackgroundColor())
                }

                dialogTabLayout.setTabTextColors(textColor, textColor)
                dialogTabLayout.setSelectedTabIndicatorColor(root.context.getProperPrimaryColor())
                dialogTabLayout.onTabSelectionChanged(tabSelectedAction = {
                    viewPager.currentItem = when {
                        it.text.toString().equals(
                            root.context.resources.getString(R.string.pattern),
                            true
                        ) -> PROTECTION_PATTERN

                        it.text.toString().equals(
                            root.context.resources.getString(R.string.pin),
                            true
                        ) -> PROTECTION_PIN

                        else -> PROTECTION_FINGERPRINT
                    }
                    updateTabVisibility()
                })
            } else {
                dialogTabLayout.beGone()
                viewPager.currentItem = showTabIndex
                viewPager.allowSwiping = false
            }
        }

        activity.getAlertDialogBuilder()
            .setOnCancelListener { onCancelFail() }
            .setNegativeButton(R.string.cancel) { _, _ -> onCancelFail() }
            .apply {
                activity.setupDialogStuff(view.root, this) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun onCancelFail() {
        callback("", 0, false)
        dialog?.dismiss()
    }

    override fun receivedHash(hash: String, type: Int) {
        callback(hash, type, true)
        if (!activity.isFinishing) {
            try {
                dialog?.dismiss()
            } catch (ignored: Exception) {
            }
        }
    }

    private fun updateTabVisibility() {
        for (i in 0..2) {
            tabsAdapter.isTabVisible(i, viewPager.currentItem == i)
        }
    }

    private fun shouldShowBiometricIdTab() = activity.isBiometricIdAvailable()

}
