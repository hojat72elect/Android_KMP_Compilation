

package com.duckduckgo.voice.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VoiceSearchPermissionCheck {
    fun hasRequiredPermissionsGranted(): Boolean
}

@ContributesBinding(ActivityScope::class)
class RealVoiceVoiceSearchPermissionCheck @Inject constructor(
    private val context: Context,
) : VoiceSearchPermissionCheck {
    override fun hasRequiredPermissionsGranted(): Boolean = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO,
    ) == PackageManager.PERMISSION_GRANTED
}
