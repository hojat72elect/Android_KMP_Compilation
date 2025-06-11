

package com.duckduckgo.app.trackerdetection

import android.net.Uri
import androidx.annotation.WorkerThread
import com.duckduckgo.app.trackerdetection.model.Entity

interface EntityLookup {
    @WorkerThread
    fun entityForUrl(url: String): Entity?

    @WorkerThread
    fun entityForUrl(url: Uri): Entity?

    @WorkerThread
    fun entityForName(name: String): Entity?
}
