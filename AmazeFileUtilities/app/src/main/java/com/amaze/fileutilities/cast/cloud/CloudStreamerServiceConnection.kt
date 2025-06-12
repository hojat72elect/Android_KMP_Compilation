package com.amaze.fileutilities.cast.cloud

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.amaze.fileutilities.home_page.ui.options.CastActivity
import com.amaze.fileutilities.utilis.ObtainableServiceBinder
import java.lang.ref.WeakReference

class CloudStreamerServiceConnection(
    private val activityRef:
    WeakReference<CastActivity>
) : ServiceConnection {
    private var specificService: CloudStreamerService? = null
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder: ObtainableServiceBinder<out CloudStreamerService?> =
            service as ObtainableServiceBinder<out CloudStreamerService?>
        specificService = binder.service
        specificService?.let { service ->
            activityRef.get()?.apply {
                this.cloudStreamerService = service
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        activityRef.get()?.cloudStreamerService = null
    }
}
