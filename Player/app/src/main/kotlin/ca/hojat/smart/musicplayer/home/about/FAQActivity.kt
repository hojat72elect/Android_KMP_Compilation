package ca.hojat.smart.musicplayer.home.about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import ca.hojat.smart.musicplayer.shared.ui.compose.extensions.enableEdgeToEdgeSimple
import ca.hojat.smart.musicplayer.shared.ui.compose.screens.FAQScreen
import ca.hojat.smart.musicplayer.shared.ui.compose.theme.AppThemeSurface
import ca.hojat.smart.musicplayer.shared.helpers.APP_FAQ
import ca.hojat.smart.musicplayer.shared.data.models.FAQItem
import kotlinx.collections.immutable.toImmutableList

class FAQActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val faqItems = remember { intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem> }
                FAQScreen(
                    goBack = ::finish,
                    faqItems = faqItems.toImmutableList()
                )
            }
        }
    }
}
