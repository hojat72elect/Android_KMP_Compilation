package ca.hojat.gamehub.common_ui

import androidx.compose.runtime.staticCompositionLocalOf
import ca.hojat.gamehub.core.providers.NetworkStateProvider
import ca.hojat.gamehub.core.sharers.TextSharer
import ca.hojat.gamehub.core.urlopeners.UrlOpener
import androidx.compose.runtime.ProvidableCompositionLocal
import android.content.Context
import ca.hojat.gamehub.core.downloader.Downloader

/**
 * As much as I have realized, these [ProvidableCompositionLocal]s are referring to the stuff that
 * we're supposed to do in ViewModels or UseCases. But the problem is that for doing any of these,
 * we need to have a [Context] instance. In order to refuse memory leak and make our architecture
 * cleaner, they are injected to our composables tree in here, so we can use them in our composables
 * but also calling them in ViewModels.
 *
 */

val LocalUrlOpener = staticCompositionLocalOf<UrlOpener> {
    error("UrlOpener not provided.")
}

val LocalTextSharer = staticCompositionLocalOf<TextSharer> {
    error("TextSharer not provided.")
}

val LocalNetworkStateProvider = staticCompositionLocalOf<NetworkStateProvider> {
    error("NetworkStateProvider not provided.")
}

val LocalDownloader = staticCompositionLocalOf<Downloader> {
    error("Downloader not provided.")
}