

package com.duckduckgo.common.utils

import android.os.Looper

fun checkMainThread() {
    check(Looper.myLooper() != Looper.getMainLooper()) {
        "Not expected to be called on the main thread but was "
    }
}
