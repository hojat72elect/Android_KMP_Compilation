

package com.duckduckgo.app.fire

import android.os.SystemClock
import com.duckduckgo.app.settings.clear.ClearWhenOption
import timber.log.Timber

interface BackgroundTimeKeeper {
    fun hasEnoughTimeElapsed(
        timeNow: Long = SystemClock.elapsedRealtime(),
        backgroundedTimestamp: Long,
        clearWhenOption: ClearWhenOption,
    ): Boolean
}

class DataClearerTimeKeeper : BackgroundTimeKeeper {

    override fun hasEnoughTimeElapsed(
        timeNow: Long,
        backgroundedTimestamp: Long,
        clearWhenOption: ClearWhenOption,
    ): Boolean {
        if (clearWhenOption == ClearWhenOption.APP_EXIT_ONLY) return false

        val elapsedTime = timeSinceAppBackgrounded(timeNow, backgroundedTimestamp)
        Timber.i("It has been ${elapsedTime}ms since the app was backgrounded. Current configuration is for $clearWhenOption")

        return elapsedTime >= clearWhenOption.durationMilliseconds()
    }

    private fun timeSinceAppBackgrounded(
        timeNow: Long,
        backgroundedTimestamp: Long,
    ): Long {
        return timeNow - backgroundedTimestamp
    }
}
