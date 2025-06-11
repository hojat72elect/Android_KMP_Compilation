package ca.hojat.gamehub.core

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object SdkInfo {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    @JvmField
    val IS_AT_LEAST_11 = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    @JvmField
    val IS_AT_LEAST_MARSHMALLOW = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
}
