package ca.hojat.smart.gallery.feature_about

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import ca.hojat.smart.gallery.R
import ca.hojat.smart.gallery.shared.data.domain.LanguageContributor
import ca.hojat.smart.gallery.shared.ui.extensions.enableEdgeToEdgeSimple
import ca.hojat.smart.gallery.shared.ui.theme.AppThemeSurface
import kotlinx.collections.immutable.toImmutableList

class ContributorsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val contributors = remember {
                    languageContributors()
                }
                val showContributorsLabel = remember {
                    !resources.getBoolean(R.bool.hide_all_external_links)
                }
                ContributorsScreen(
                    goBack = ::finish,
                    showContributorsLabel = showContributorsLabel,
                    contributors = contributors,
                )
            }
        }
    }

    private fun languageContributors() = listOf(
        LanguageContributor(
            R.drawable.ic_flag_persian_vector,
            R.string.translation_persian,
            R.string.translators_persian
        ),
    ).toImmutableList()
}
