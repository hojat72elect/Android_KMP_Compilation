

package com.duckduckgo.app.browser.applinks

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Browser.EXTRA_APPLICATION_ID
import android.widget.Toast
import androidx.annotation.StringRes
import com.duckduckgo.app.browser.BrowserTabViewModel
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.SpecialUrlDetector.UrlType.AppLink
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

interface AppLinksLauncher {
    fun openAppLink(context: Context?, appLink: AppLink, viewModel: BrowserTabViewModel)
}

@ContributesBinding(AppScope::class)
class DuckDuckGoAppLinksLauncher @Inject constructor() : AppLinksLauncher {

    override fun openAppLink(context: Context?, appLink: AppLink, viewModel: BrowserTabViewModel) {
        if (context == null) return
        appLink.appIntent?.let {
            configureIntent(it, context)
            startActivityOrQuietlyFail(context, it)
        }
        viewModel.clearPreviousUrl()
    }

    private fun configureIntent(
        intent: Intent,
        context: Context,
    ) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.selector?.addCategory(Intent.CATEGORY_BROWSABLE)

        intent.component = null
        intent.selector?.component = null

        intent.putExtra(EXTRA_APPLICATION_ID, context.packageName)
    }

    private fun startActivityOrQuietlyFail(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            Timber.e(exception, "Activity not found")
        } catch (exception: SecurityException) {
            showToast(context, R.string.unableToOpenLink)
        }
    }

    private fun showToast(context: Context, @StringRes messageId: Int, length: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context.applicationContext, messageId, length).show()
    }
}
