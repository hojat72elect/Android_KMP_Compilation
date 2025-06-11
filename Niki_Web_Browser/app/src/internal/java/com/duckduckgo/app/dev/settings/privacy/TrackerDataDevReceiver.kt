package com.duckduckgo.app.dev.settings.privacy

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.trackerdetection.api.TrackerDataDownloader
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.extensions.registerNotExportedReceiver
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TrackerDataDevReceiver(
    context: Context,
    intentAction: String = DOWNLOAD_TDS_INTENT_ACTION,
    private val receiver: (Intent) -> Unit,
) : BroadcastReceiver() {
    init {
        context.registerNotExportedReceiver(this, IntentFilter(intentAction))
    }

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        receiver(intent)
    }

    companion object {
        const val DOWNLOAD_TDS_INTENT_ACTION = "downloadTds"
    }
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class TrackerDataDevReceiverRegister @Inject constructor(
    private val context: Context,
    private val trackderDataDownloader: TrackerDataDownloader,
    private val appBuildConfig: AppBuildConfig,
) : MainProcessLifecycleObserver {

    @SuppressLint("CheckResult")
    override fun onCreate(owner: LifecycleOwner) {
        if (!appBuildConfig.isDebug) {
            Timber.i("Will not register TrackerDataDevReceiverRegister, not in DEBUG mode")
            return
        }

        Timber.i("Debug receiver TrackerDataDevReceiverRegister registered")

        TrackerDataDevReceiver(context) { _ ->
            trackderDataDownloader.downloadTds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Toast.makeText(context, "Tds data downloaded", Toast.LENGTH_LONG).show() },
                    { Toast.makeText(context, "Error while downloading Tds ${it.localizedMessage}", Toast.LENGTH_LONG).show() },
                )
        }
    }
}
