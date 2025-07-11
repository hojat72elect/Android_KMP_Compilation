package ca.hojat.notes.niki.feature_about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import ca.hojat.notes.niki.feature_about.FAQScreen
import ca.hojat.notes.niki.shared.ui.compose.extensions.enableEdgeToEdgeSimple
import ca.hojat.notes.niki.shared.ui.compose.theme.AppThemeSurface
import ca.hojat.notes.niki.shared.helpers.APP_FAQ
import ca.hojat.notes.niki.shared.data.models.FAQItem
import kotlinx.collections.immutable.toImmutableList

class FAQActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val faqItems =
                    remember { intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem> }
                FAQScreen(
                    goBack = ::finish,
                    faqItems = faqItems.toImmutableList()
                )
            }
        }
    }
}
