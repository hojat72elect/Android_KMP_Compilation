

package com.duckduckgo.app.global

import android.os.Looper

fun Thread.isAndroidMainThread(): Boolean =
    Thread.currentThread().id == Looper.getMainLooper().thread.id
