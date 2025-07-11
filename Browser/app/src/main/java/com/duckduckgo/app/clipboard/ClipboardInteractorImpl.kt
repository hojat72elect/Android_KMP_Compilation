

package com.duckduckgo.app.clipboard

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build.VERSION_CODES
import android.os.PersistableBundle
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class ClipboardInteractorImpl @Inject constructor(
    context: Context,
    private val appBuildConfig: AppBuildConfig,
) : ClipboardInteractor {
    private val clipboardManager by lazy { context.getSystemService(ClipboardManager::class.java) }

    @SuppressLint("NewApi")
    override fun copyToClipboard(
        toCopy: String,
        isSensitive: Boolean,
    ): Boolean {
        val clipData = ClipData.newPlainText("", toCopy)
        if (isSensitive) {
            clipData.description.extras = PersistableBundle().apply {
                putBoolean("android.content.extra.IS_SENSITIVE", true)
            }
        }
        clipboardManager.setPrimaryClip(clipData)

        return notificationAutomaticallyShown()
    }

    private fun notificationAutomaticallyShown(): Boolean {
        // Samsung on Android 12 shows its own toast when copying text
        if (appBuildConfig.manufacturer == "samsung" && (appBuildConfig.sdkInt == VERSION_CODES.S || appBuildConfig.sdkInt == VERSION_CODES.S_V2)) {
            return true
        }

        // From Android 13, the system shows its own toast when copying text, so we don't want to show our own
        return appBuildConfig.sdkInt >= VERSION_CODES.TIRAMISU
    }
}
