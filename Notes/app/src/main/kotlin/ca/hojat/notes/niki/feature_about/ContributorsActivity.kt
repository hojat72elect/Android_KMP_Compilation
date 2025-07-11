package ca.hojat.notes.niki.feature_about

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.feature_about.ContributorsScreen
import ca.hojat.notes.niki.shared.ui.compose.extensions.enableEdgeToEdgeSimple
import ca.hojat.notes.niki.shared.ui.compose.theme.AppThemeSurface
import ca.hojat.notes.niki.shared.data.models.LanguageContributor
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
