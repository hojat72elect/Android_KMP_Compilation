@file:JvmName("ContextUtils") @file:Suppress("TooManyFunctions")

package ca.hojat.gamehub.core.extensions

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

val Context.configuration: Configuration
    get() = resources.configuration

fun Context.getCompatColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.showShortToast(message: CharSequence): Toast {
    return showToast(message, duration = Toast.LENGTH_SHORT)
}

fun Context.showLongToast(message: CharSequence): Toast {
    return showToast(message, duration = Toast.LENGTH_LONG)
}

fun Context.showToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, message, duration).apply { show() }
}

inline fun <reified T : Any> Context.getSystemService(): T {
    return checkNotNull(ContextCompat.getSystemService(this, T::class.java)) {
        "The service ${T::class.java.simpleName} could not be retrieved."
    }
}

@get:Suppress("DEPRECATION")
@get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
val Context.isConnectedToNetwork: Boolean
    get() = (getSystemService<ConnectivityManager>().activeNetworkInfo?.isConnected == true)
