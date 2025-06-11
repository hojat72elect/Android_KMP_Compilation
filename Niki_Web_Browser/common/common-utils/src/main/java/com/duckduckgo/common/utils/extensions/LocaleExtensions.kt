

package com.duckduckgo.common.utils.extensions

import java.util.Locale
import java.util.Locale.UNICODE_LOCALE_EXTENSION

fun Locale.toSanitizedLanguageTag(): String {
    return Locale.Builder().setLocale(this).setExtension(UNICODE_LOCALE_EXTENSION, "").build().toLanguageTag()
}
