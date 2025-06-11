

package com.duckduckgo.app.fire

import com.duckduckgo.app.settings.clear.ClearWhenOption
import java.util.concurrent.TimeUnit

fun ClearWhenOption.durationMilliseconds(): Long {
    return when (this) {
        ClearWhenOption.APP_EXIT_ONLY -> 0
        ClearWhenOption.APP_EXIT_OR_5_MINS -> TimeUnit.MINUTES.toMillis(5)
        ClearWhenOption.APP_EXIT_OR_15_MINS -> TimeUnit.MINUTES.toMillis(15)
        ClearWhenOption.APP_EXIT_OR_30_MINS -> TimeUnit.MINUTES.toMillis(30)
        ClearWhenOption.APP_EXIT_OR_60_MINS -> TimeUnit.MINUTES.toMillis(60)
        ClearWhenOption.APP_EXIT_OR_5_SECONDS -> TimeUnit.SECONDS.toMillis(5)
    }
}
