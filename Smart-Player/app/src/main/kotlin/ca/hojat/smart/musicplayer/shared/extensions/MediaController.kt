package ca.hojat.smart.musicplayer.shared.extensions

import android.os.Bundle
import androidx.media3.session.MediaController
import ca.hojat.smart.musicplayer.shared.playback.CustomCommands

fun MediaController.sendCommand(command: CustomCommands, extras: Bundle = Bundle.EMPTY) = sendCustomCommand(command.sessionCommand, extras)
