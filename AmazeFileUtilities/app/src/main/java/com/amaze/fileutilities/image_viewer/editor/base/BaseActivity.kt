package com.amaze.fileutilities.image_viewer.editor.base

import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amaze.fileutilities.R
import com.amaze.fileutilities.utilis.Utils.Companion.showProcessingDialog
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private var mProgressDialog: AlertDialog? = null
    private var mPermission: String? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isPermissionGranted(it, mPermission)
        }

    fun requestPermission(permission: String): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            mPermission = permission
            permissionLauncher.launch(permission)
        }
        return isGranted
    }

    open fun isPermissionGranted(isGranted: Boolean, permission: String?) {}

    protected fun showLoading(message: String) {
        mProgressDialog = showProcessingDialog(
            layoutInflater,
            message
        ).create()
        mProgressDialog?.show()
    }

    protected fun hideLoading() {
        mProgressDialog?.dismiss()
    }

    protected fun showSnackbar(message: String) {
        val view = findViewById<View>(R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
