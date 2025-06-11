

package com.duckduckgo.httpsupgrade.api

import android.net.Uri
import androidx.annotation.WorkerThread

interface HttpsUpgrader {

    @WorkerThread
    fun shouldUpgrade(uri: Uri): Boolean

    fun upgrade(uri: Uri): Uri

    @WorkerThread
    fun reloadData()
}
