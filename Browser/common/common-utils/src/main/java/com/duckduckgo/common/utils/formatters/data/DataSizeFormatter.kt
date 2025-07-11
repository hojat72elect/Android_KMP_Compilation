

package com.duckduckgo.common.utils.formatters.data

import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named

class DataSizeFormatter @Inject constructor(@Named("numberFormatterWithSeparator") val numberFormatter: NumberFormat) {

    fun format(bytes: Long): String {
        return when {
            (bytes >= BYTES_PER_GIGABYTE) -> {
                val formatted = (bytes.toDouble() / BYTES_PER_GIGABYTE)
                "${numberFormatter.format(formatted)} GB"
            }
            (bytes >= BYTES_PER_MEGABYTE) -> {
                val formatted = bytes.toDouble() / BYTES_PER_MEGABYTE
                "${numberFormatter.format(formatted)} MB"
            }

            (bytes >= BYTES_PER_KILOBYTE) -> {
                val formatted = bytes.toDouble() / BYTES_PER_KILOBYTE
                "${numberFormatter.format(formatted)} KB"
            }

            else -> "${numberFormatter.format(bytes)} bytes"
        }
    }

    companion object {
        private const val BYTES_PER_KILOBYTE = 1_000
        private const val BYTES_PER_MEGABYTE = 1_000_000
        private const val BYTES_PER_GIGABYTE = 1_000_000_000
    }
}
