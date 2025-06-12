package com.amaze.fileutilities.home_page

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.amaze.fileutilities.PermissionsActivity
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.welcome_helper.WelcomeActivity
import com.amaze.fileutilities.home_page.welcome_helper.WelcomeUtils
import com.amaze.fileutilities.utilis.showToastInCenter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Remove duplication of this class with PermissionActivity
 */
abstract class WelcomePermissionScreen :
    WelcomeActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    private var log: Logger = LoggerFactory.getLogger(WelcomePermissionScreen::class.java)

    fun haveStoragePermissions(): Boolean {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkStoragePermission()) {
                return false
            }
        }
        return true
    }

    companion object {
        private const val PERMISSION_LENGTH = 5
        private const val STORAGE_PERMISSION = 0
        private const val ALL_FILES_PERMISSION = 1
        private const val LOCATION_PERMISSION = 2
    }

    private val permissionCallbacks = arrayOfNulls<OnPermissionGranted>(PERMISSION_LENGTH)

    val onPermissionGranted = object : OnPermissionGranted {
        override fun onPermissionGranted(isGranted: Boolean) {
            if (isGranted) {
                //nothing
            } else {
                Toast.makeText(
                    this@WelcomePermissionScreen, R.string.grantfailed,
                    Toast.LENGTH_SHORT
                ).show()
                requestStoragePermission(
                    permissionCallbacks[STORAGE_PERMISSION]!!,
                    false
                )
            }
            permissionCallbacks[STORAGE_PERMISSION] = null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            permissionCallbacks[STORAGE_PERMISSION]?.onPermissionGranted(isGranted(grantResults))
        } else if (requestCode == LOCATION_PERMISSION) {
            if (isGranted(grantResults)) {

                permissionCallbacks[LOCATION_PERMISSION]!!.onPermissionGranted(true)
                permissionCallbacks[LOCATION_PERMISSION] = null
            } else {
                Toast.makeText(
                    this, R.string.grant_location_failed,
                    Toast.LENGTH_SHORT
                ).show()

                requestStoragePermission(
                    permissionCallbacks[LOCATION_PERMISSION]!!,
                    false
                )
                permissionCallbacks[LOCATION_PERMISSION]!!.onPermissionGranted(false)
                permissionCallbacks[LOCATION_PERMISSION] = null

            }
        }
    }

    fun checkStoragePermission(): Boolean {
        // Verify that all required contact permissions have been granted.
        var isFound = false
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                isFound = (
                        ActivityCompat.checkSelfPermission(
                            this, Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        )
                                == PackageManager.PERMISSION_GRANTED
                        ) || (
                        ActivityCompat.checkSelfPermission(
                            this, Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        )

                                == PackageManager.PERMISSION_GRANTED
                        ) || Environment.isExternalStorageManager()
            } catch (anfe: ActivityNotFoundException) {
                log.warn("all files access permission activity missing, fallback to default", anfe)
            }
        }
        if (!isFound) {
            isFound = (
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                            == PackageManager.PERMISSION_GRANTED
                    )
        }
        return isFound
    }


    fun requestStoragePermission(
        onPermissionGranted: OnPermissionGranted,
        isInitialStart: Boolean
    ) {
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it, R.style.Custom_Dialog_Dark)
        }
        builder.setMessage(R.string.grant_storage_read_permission)
            .setTitle(R.string.grant_permission)
            .setNegativeButton(R.string.cancel) { _, _ ->
                finish()
            }.setCancelable(false)
        requestPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION,
            builder,
            onPermissionGranted,
            isInitialStart
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission(
        onPermissionGranted: OnPermissionGranted,
        isInitialStart: Boolean
    ) {
//        Utils.disableScreenRotation(this)
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it, R.style.Custom_Dialog_Dark)
        }
        builder.setMessage(R.string.grant_notification_permission)
            .setTitle(R.string.grant_permission)
            .setNegativeButton(R.string.cancel) { _, _ ->
                finish()
            }.setCancelable(false)
        requestPermission(
            Manifest.permission.POST_NOTIFICATIONS,
            PermissionsActivity.NOTIFICATION_PERMISSION,
            builder,
            onPermissionGranted,
            isInitialStart
        )
    }

    /**
     * Requests permission, overrides {@param rationale}'s POSITIVE button dialog action.
     *
     * @param permission The permission to ask for
     * @param code [.STORAGE_PERMISSION] or [.INSTALL_APK_PERMISSION]
     * @param rationale MaterialLayout to provide an additional rationale to the user if the
     * permission was not granted and the user would benefit from additional context for the use
     * of the permission. For example, if the request has been denied previously.
     * @param isInitialStart is the permission being requested for the first time in the application
     * lifecycle
     */
    private fun requestPermission(
        permission: String,
        code: Int,
        rationale: AlertDialog.Builder,
        onPermissionGranted: OnPermissionGranted,
        isInitialStart: Boolean
    ) {
        permissionCallbacks[code] = onPermissionGranted
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            rationale
                .setPositiveButton(R.string.grant) { dialog, _ ->
                    run {
                        ActivityCompat.requestPermissions(
                            this@WelcomePermissionScreen, arrayOf(permission), code
                        )
                        dialog.cancel()
                    }
                }
            rationale.show()
        } else if (isInitialStart) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), code)
        } else {
            /*Snackbar.make(
                findViewById(R.id.frameLayout),
                R.string.grantfailed,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            )
                .setAction(
                    R.string.grant
                ) { v ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse(java.lang.String.format("package:%s", packageName))
                        )
                    )
                }
                .show()*/
            applicationContext.showToastInCenter(getString(R.string.grantfailed))
            finish()
        }
    }

    /**
     * Request all files access on android 11+
     *
     * @param onPermissionGranted permission granted callback
     */
    fun requestAllFilesAccess(onPermissionGranted: OnPermissionGranted) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            !Environment.isExternalStorageManager()
        ) {
            val builder: AlertDialog.Builder = this.let {
                AlertDialog.Builder(it, R.style.Custom_Dialog_Dark)
            }
            builder.setMessage(R.string.grant_all_files_permission)
                .setTitle(R.string.grant_permission)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }
                .setPositiveButton(R.string.grant) { dialog, _ ->
                    run {
//                        Utils.disableScreenRotation(this)
                        permissionCallbacks[ALL_FILES_PERMISSION] = onPermissionGranted
                        try {
                            val intent =
                                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                                    .setData(Uri.parse("package:$packageName"))
                            startActivity(intent)
                        } catch (anf: ActivityNotFoundException) {
                            // fallback
                            log.warn("Failed to find activity for all files access", anf)
                            try {
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                        .setData(Uri.parse("package:$packageName"))
                                startActivity(intent)
                            } catch (e: Exception) {
                                log.error(
                                    "Failed to initial activity to grant all files access",
                                    e
                                )
                                applicationContext.showToastInCenter(
                                    getString(
                                        R.string
                                            .grantfailed
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            log.error("Failed to grant all files access", e)
                            applicationContext.showToastInCenter(getString(R.string.grantfailed))
                        }
                        dialog.cancel()
                    }
                }.setCancelable(false).show()
        }
    }

    private fun isGranted(grantResults: IntArray): Boolean {
        return grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    /**
     *  Remove all flags and URI that put into the Intent.
     *  We are finishing the app here, so any URI provided will not be useful anyway.
     */
    override fun cancelWelcomeScreen() {
        val intent = this.intent
        intent.flags = -1
        intent.data = null
        intent.putExtra(WELCOME_SCREEN_KEY, WelcomeUtils.getKey(this.javaClass))
        this.setResult(RESULT_CANCELED, intent)
        finish()
    }

    interface OnPermissionGranted {
        fun onPermissionGranted(isGranted: Boolean)
    }
}
