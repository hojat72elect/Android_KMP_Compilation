

package com.duckduckgo.app.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.widget.AppWidgetManagerAddWidgetLauncher.Companion.ACTION_ADD_WIDGET
import com.duckduckgo.common.utils.extensions.registerNotExportedReceiver
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class WidgetAddedReceiver @Inject constructor(
    private val context: Context,
) : BroadcastReceiver(), MainProcessLifecycleObserver {

    companion object {
        val IGNORE_MANUFACTURERS_LIST = listOf("samsung", "huawei")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        context.registerNotExportedReceiver(this, IntentFilter(ACTION_ADD_WIDGET))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        context.unregisterReceiver(this)
    }

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        if (!IGNORE_MANUFACTURERS_LIST.contains(Build.MANUFACTURER)) {
            context?.let {
                val title = intent?.getStringExtra(AppWidgetManagerAddWidgetLauncher.EXTRA_WIDGET_ADDED_LABEL) ?: ""
                Toast.makeText(it, it.getString(R.string.homeScreenWidgetAdded, title), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
