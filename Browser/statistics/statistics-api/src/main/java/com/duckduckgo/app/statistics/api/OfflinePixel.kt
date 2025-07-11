

package com.duckduckgo.app.statistics.api

import io.reactivex.Completable

/**
 * Implement this interface and return a multibinding when you want to schedule the sending
 * of pixels.
 * The use case is generally when you have a DB with some pixels to be sent periodically.
 * You can use this ready-made infrastructure instead of creating your own worker etc.
 *
 * Example:
 * <pre><code>
 * @ContributesMultibinding(AppScope::class)
 * class MyOfflinePixelSender @Inject constructor(...) {
 *   override fun send(): Completable {
 *     return defer {
 *       // get stored data
 *       // send pixel(s)
 *     }
 *   }
 * }
 * </code></pre>
 */
interface OfflinePixel {
    fun send(): Completable
}
