package ca.hojat.smart.musicplayer.shared.helpers

import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.writeLn
import ca.hojat.smart.musicplayer.shared.data.models.Track
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase
import java.io.OutputStream

class M3uExporter(val activity: BaseSimpleActivity) {
    private var failedEvents = 0
    private var exportedEvents = 0

    enum class ExportResult {
        EXPORT_FAIL, EXPORT_OK, EXPORT_PARTIAL
    }

    fun exportPlaylist(
        outputStream: OutputStream?,
        tracks: ArrayList<Track>,
        callback: (result: ExportResult) -> Unit
    ) {
        if (outputStream == null) {
            callback(ExportResult.EXPORT_FAIL)
            return
        }

        ShowToastUseCase(activity, R.string.exporting)

        try {
            outputStream.bufferedWriter().use { out ->
                out.writeLn(M3U_HEADER)
                for (track in tracks) {
                    out.writeLn(M3U_ENTRY + track.duration + M3U_DURATION_SEPARATOR + track.artist + " - " + track.title)
                    out.writeLn(track.path)
                    exportedEvents++
                }
            }
        } catch (e: Exception) {
            failedEvents++
            ShowToastUseCase(activity, "The error : $e")
        } finally {
            outputStream.close()
        }

        callback(
            when {
                exportedEvents == 0 -> ExportResult.EXPORT_FAIL
                failedEvents > 0 -> ExportResult.EXPORT_PARTIAL
                else -> ExportResult.EXPORT_OK
            }
        )
    }
}
