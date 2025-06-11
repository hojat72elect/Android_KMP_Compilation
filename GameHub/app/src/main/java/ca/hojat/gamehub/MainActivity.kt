package ca.hojat.gamehub

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import ca.hojat.gamehub.common_ui.LocalDownloader
import ca.hojat.gamehub.common_ui.LocalNetworkStateProvider
import ca.hojat.gamehub.common_ui.LocalTextSharer
import ca.hojat.gamehub.common_ui.LocalUrlOpener
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import ca.hojat.gamehub.core.domain.common.usecases.execute
import ca.hojat.gamehub.core.downloader.Downloader
import ca.hojat.gamehub.core.providers.NetworkStateProvider
import ca.hojat.gamehub.core.sharers.TextSharer
import ca.hojat.gamehub.core.urlopeners.UrlOpener
import ca.hojat.gamehub.feature_settings.domain.entities.Settings
import ca.hojat.gamehub.feature_settings.domain.entities.Theme
import ca.hojat.gamehub.feature_settings.domain.usecases.ObserveThemeUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var urlOpener: UrlOpener

    @Inject
    lateinit var textSharer: TextSharer

    @Inject
    lateinit var networkStateProvider: NetworkStateProvider

    @Inject
    lateinit var downloader: Downloader

    @Inject
    lateinit var observeThemeUseCase: ObserveThemeUseCase

    private var shouldKeepSplashOpen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        setupSystemBars()
        setupCompose()
    }

    private fun setupSplashScreen() {
        // Waiting until the app's theme is loaded first before
        // displaying the dashboard to prevent the user from seeing
        // the app blinking as a result of the theme change
        installSplashScreen().setKeepOnScreenCondition(::shouldKeepSplashOpen)
    }

    private fun setupSystemBars() {
        // To be able to draw behind system bars & change their colors
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun setupCompose() {
        setContent {
            CompositionLocalProvider(LocalUrlOpener provides urlOpener) {
                CompositionLocalProvider(LocalTextSharer provides textSharer) {
                    CompositionLocalProvider(LocalNetworkStateProvider provides networkStateProvider) {
                        CompositionLocalProvider(LocalDownloader provides downloader) {
                            GameHubTheme(useDarkTheme = shouldUseDarkTheme()) {
                                MainScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun shouldUseDarkTheme(): Boolean {
        val themeState = observeThemeUseCase.execute().collectAsState(initial = null)
        val theme = (themeState.value ?: Settings.DEFAULT.theme)

        LaunchedEffect(Unit) {
            snapshotFlow { themeState.value }
                .filterNotNull()
                .collect {
                    if (shouldKeepSplashOpen) {
                        shouldKeepSplashOpen = false
                    }
                }
        }

        return when (theme) {
            Theme.LIGHT -> false
            Theme.DARK -> true
            Theme.SYSTEM -> isSystemInDarkTheme()
        }
    }
}
