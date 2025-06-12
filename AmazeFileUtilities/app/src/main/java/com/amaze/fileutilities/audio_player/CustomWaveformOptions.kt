package com.amaze.fileutilities.audio_player

import android.content.Context
import linc.com.amplituda.Amplituda
import linc.com.amplituda.AmplitudaProcessingOutput
import linc.com.amplituda.exceptions.AmplitudaException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal object CustomWaveformOptions {

    var log: Logger = LoggerFactory.getLogger(CustomWaveformOptions::class.java)

    @JvmStatic
    fun getSampleFrom(context: Context, pathOrUrl: String): IntArray? {
        val processingOutput: AmplitudaProcessingOutput<String>
        try {
            processingOutput = Amplituda(context).processAudio(pathOrUrl)
        } catch (e: Exception) {
            log.warn("failed to get processing output", e)
            return null
        }
        return handleAmplitudaOutput(pathOrUrl, processingOutput)
    }

    private fun handleAmplitudaOutput(
        path: String,
        amplitudaOutput: AmplitudaProcessingOutput<*>
    ): IntArray {
        val result = amplitudaOutput.get { e: AmplitudaException ->
            log.warn("failed to get waveform data from amplituda for path {}", path, e)
        }
        return result.amplitudesAsList().toTypedArray().toIntArray()
    }
}
