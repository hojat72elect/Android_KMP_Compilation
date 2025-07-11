

package com.duckduckgo.app.browser.camera

import android.content.Context
import android.content.pm.PackageManager.FEATURE_CAMERA_ANY
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface CameraHardwareChecker {
    fun hasCameraHardware(): Boolean
}

@ContributesBinding(AppScope::class)
class CameraHardwareCheckerImpl @Inject constructor(
    private val context: Context,
) : CameraHardwareChecker {

    override fun hasCameraHardware(): Boolean {
        return with(context.packageManager) {
            kotlin.runCatching { hasSystemFeature(FEATURE_CAMERA_ANY) }.getOrDefault(false)
        }
    }
}
