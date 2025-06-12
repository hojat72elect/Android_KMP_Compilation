package ca.hojat.notes.niki.shared.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import ca.hojat.notes.niki.shared.extensions.backupNotes

class AutomaticBackupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakelock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "simplenotes:automaticbackupreceiver"
        )
        wakelock.acquire(3000)
        context.backupNotes()
    }
}
