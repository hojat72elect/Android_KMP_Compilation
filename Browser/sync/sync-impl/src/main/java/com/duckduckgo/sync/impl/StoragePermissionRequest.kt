

package com.duckduckgo.sync.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.*

interface PermissionRequest {
    fun registerResultsCallback(
        caller: ActivityResultCaller,
        onPermissionDenied: () -> Unit,
    )

    fun invokeOrRequestPermission(
        invoke: () -> Unit,
    )
}

@ContributesBinding(ActivityScope::class)
class StoragePermissionRequest @Inject constructor(
    val appBuildConfig: AppBuildConfig,
    val context: Context,
) : PermissionRequest {

    private var permissionRequest: ActivityResultLauncher<String>? = null

    private var onPermissionGranted: (() -> Unit)? = null
    override fun registerResultsCallback(
        caller: ActivityResultCaller,
        onPermissionDenied: () -> Unit,
    ) {
        permissionRequest = caller.registerForActivityResult(RequestPermission()) {
            if (it) {
                onPermissionGranted?.invoke()
            } else {
                onPermissionDenied.invoke()
            }
        }
    }

    override fun invokeOrRequestPermission(
        invoke: () -> Unit,
    ) {
        val permissionRequest = permissionRequest ?: throw IllegalStateException("registerResultsCallback must be called before invoking this method")

        if (hasWriteStoragePermission()) {
            invoke.invoke()
        } else {
            onPermissionGranted = invoke
            permissionRequest.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun hasWriteStoragePermission(): Boolean {
        return minSdk30() ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun minSdk30(): Boolean {
        return appBuildConfig.sdkInt >= Build.VERSION_CODES.R
    }
}
