

package com.duckduckgo.httpsupgrade.api

import io.reactivex.Completable

interface HttpsUpgradeDataDownloader {
    fun download(): Completable
}
