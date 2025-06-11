package ca.hojat.gamehub.core.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atZone(zoneId).toInstant().toEpochMilli()
}
