

package com.duckduckgo.voice.impl

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface PermissionRationale {
    fun shouldShow(activity: Activity): Boolean
}

@ContributesBinding(ActivityScope::class)
class RealPermissionRationale @Inject constructor() : PermissionRationale {
    override fun shouldShow(activity: Activity): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
}
