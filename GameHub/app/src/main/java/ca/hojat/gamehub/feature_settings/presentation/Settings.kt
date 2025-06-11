package ca.hojat.gamehub.feature_settings.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import ca.hojat.gamehub.common_ui.theme.subtitle3
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.CommandsHandler
import ca.hojat.gamehub.common_ui.LocalUrlOpener
import ca.hojat.gamehub.common_ui.NavBarColorHandler
import ca.hojat.gamehub.common_ui.widgets.AnimatedContentContainer
import ca.hojat.gamehub.common_ui.widgets.FiniteUiState
import ca.hojat.gamehub.common_ui.widgets.GameHubCard
import ca.hojat.gamehub.common_ui.widgets.GameNewsProgressIndicator
import ca.hojat.gamehub.common_ui.widgets.dialogs.GameNewsDialog
import ca.hojat.gamehub.common_ui.widgets.toolbars.Toolbar
import ca.hojat.gamehub.core.extensions.showShortToast
import ca.hojat.gamehub.feature_settings.domain.entities.Language
import ca.hojat.gamehub.feature_settings.domain.entities.Theme

@Composable
fun Settings(modifier: Modifier) {
    Settings(
        viewModel = hiltViewModel(),
        modifier = modifier,
    )
}

@Composable
private fun Settings(
    viewModel: SettingsViewModel,
    modifier: Modifier,
) {
    val urlOpener = LocalUrlOpener.current
    val context = LocalContext.current

    NavBarColorHandler()
    CommandsHandler(viewModel = viewModel) { command ->
        when (command) {
            is SettingsCommand.OpenUrl -> {
                if (!urlOpener.openUrl(command.url, context)) {
                    context.showShortToast(context.getString(R.string.url_opener_not_found))
                }
            }
        }
    }
    Settings(
        uiState = viewModel.uiState.collectAsState().value,
        onSettingClicked = viewModel::onSettingClicked,
        onThemePicked = viewModel::onThemePicked,
        onThemePickerDismissed = viewModel::onThemePickerDismissed,
        onLanguagePicked = viewModel::onLanguagePicked,
        onLanguagePickerDismissed = viewModel::onLanguagePickerDismissed,
        modifier = modifier,
    )
}

@Composable
private fun Settings(
    uiState: SettingsUiState,
    onSettingClicked: (SettingsSectionItemUiModel) -> Unit,
    onThemePicked: (Theme) -> Unit,
    onThemePickerDismissed: () -> Unit,
    onLanguagePicked: (Language) -> Unit,
    onLanguagePickerDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(R.string.settings_toolbar_title),
                contentPadding = WindowInsets.statusBars.only(WindowInsetsSides.Vertical + WindowInsetsSides.Horizontal)
                    .asPaddingValues(),
            )
        },
    ) { paddingValues ->
        AnimatedContentContainer(
            finiteUiState = uiState.finiteUiState,
            modifier = Modifier.padding(paddingValues),
        ) { finiteUiState ->
            when (finiteUiState) {
                FiniteUiState.LOADING -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }
                FiniteUiState.SUCCESS -> {
                    SuccessState(
                        sections = uiState.sections,
                        onSettingClicked = onSettingClicked,
                    )
                }
                else -> error("Unsupported finite UI state = $finiteUiState.")
            }
        }
    }

    if (uiState.isThemePickerVisible) {
        ThemePickerDialog(
            uiState = uiState,
            onThemePicked = onThemePicked,
            onPickerDismissed = onThemePickerDismissed,
        )
    }
    if (uiState.isLanguagePickerVisible) {
        LanguagePickerDialog(
            uiState = uiState,
            onLanguagePicked = onLanguagePicked,
            onPickerDismissed = onLanguagePickerDismissed
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    GameNewsProgressIndicator(modifier = modifier)
}

@Composable
private fun SuccessState(
    sections: List<SettingsSectionUiModel>,
    onSettingClicked: (SettingsSectionItemUiModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(GameHubTheme.spaces.spacing_3_5),
    ) {
        items(
            items = sections,
            key = SettingsSectionUiModel::id,
        ) { section ->
            SettingsSection(
                section = section,
                onSettingClicked = onSettingClicked,
            )
        }
    }
}

@Composable
private fun SettingsSection(
    section: SettingsSectionUiModel,
    onSettingClicked: (SettingsSectionItemUiModel) -> Unit,
) {
    GameHubCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(
                top = GameHubTheme.spaces.spacing_4_0,
                bottom = GameHubTheme.spaces.spacing_2_0,
            ),
        ) {
            Text(
                text = section.title,
                modifier = Modifier
                    .padding(horizontal = GameHubTheme.spaces.spacing_4_0)
                    .padding(bottom = GameHubTheme.spaces.spacing_2_0),
                color = GameHubTheme.colors.secondary,
                style = GameHubTheme.typography.subtitle3,
            )

            for (sectionItem in section.items) {
                SettingsSectionItem(
                    sectionItem = sectionItem,
                    contentPadding = PaddingValues(
                        vertical = GameHubTheme.spaces.spacing_2_0,
                        horizontal = GameHubTheme.spaces.spacing_4_0,
                    ),
                    onSettingClicked = onSettingClicked,
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionItem(
    sectionItem: SettingsSectionItemUiModel,
    contentPadding: PaddingValues,
    onSettingClicked: (SettingsSectionItemUiModel) -> Unit,
) {
    val clickableModifier = if (sectionItem.isClickable) {
        Modifier.clickable { onSettingClicked(sectionItem) }
    } else {
        Modifier
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(contentPadding),
    ) {
        Text(
            text = sectionItem.title,
            color = GameHubTheme.colors.onPrimary,
            style = GameHubTheme.typography.subtitle3,
        )
        Text(
            text = sectionItem.description,
            modifier = Modifier.padding(top = GameHubTheme.spaces.spacing_0_5),
            style = GameHubTheme.typography.body2,
        )
    }
}

@Composable
private fun ThemePickerDialog(
    uiState: SettingsUiState,
    onThemePicked: (Theme) -> Unit,
    onPickerDismissed: () -> Unit,
) {
    GameNewsDialog(onDialogDismissed = onPickerDismissed) {
        Text(
            text = stringResource(R.string.settings_item_theme_title),
            modifier = Modifier
                .padding(horizontal = GameHubTheme.spaces.spacing_6_0)
                .padding(bottom = GameHubTheme.spaces.spacing_2_0),
            color = GameHubTheme.colors.onPrimary,
            style = GameHubTheme.typography.h5,
        )

        for (theme in Theme.values()) {
            SingleChoiceListOption(
                isSelected = (theme.name == uiState.selectedThemeName),
                title = stringResource(theme.uiTextRes),
                onOptionClicked = { onThemePicked(theme) },
            )
        }
    }
}

@Composable
private fun LanguagePickerDialog(
    uiState: SettingsUiState,
    onLanguagePicked: (Language) -> Unit,
    onPickerDismissed: () -> Unit,
) {
    GameNewsDialog(onDialogDismissed = onPickerDismissed) {
        Text(
            text = stringResource(R.string.settings_item_language_title),
            modifier = Modifier
                .padding(horizontal = GameHubTheme.spaces.spacing_6_0)
                .padding(bottom = GameHubTheme.spaces.spacing_2_0),
            color = GameHubTheme.colors.onPrimary,
            style = GameHubTheme.typography.h5,
        )

        for (language in Language.values()) {
            SingleChoiceListOption(isSelected = (language.name == uiState.selectedLanguageName),
                title = stringResource(id = language.uiTextRes),
                onOptionClicked = { onLanguagePicked(language) })
        }
    }

}

@Composable
private fun SingleChoiceListOption(
    isSelected: Boolean,
    title: String,
    onOptionClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOptionClicked)
            .padding(
                vertical = GameHubTheme.spaces.spacing_3_0,
                horizontal = GameHubTheme.spaces.spacing_5_5,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = GameHubTheme.colors.secondary,
                unselectedColor = GameHubTheme.colors.onSurface,
            ),
        )

        Text(
            text = title,
            modifier = Modifier.padding(start = GameHubTheme.spaces.spacing_4_0),
            style = GameHubTheme.typography.h6,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsLoadingStatePreview() {
    GameHubTheme {
        Settings(uiState = SettingsUiState(
            isLoading = false,
            sections = emptyList(),
            selectedThemeName = null,
            isThemePickerVisible = false,
            selectedLanguageName = null,
            isLanguagePickerVisible = false
        ),
            onSettingClicked = {},
            onThemePicked = {},
            onThemePickerDismissed = {},
            onLanguagePicked = {},
            onLanguagePickerDismissed = {})
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsSuccessStatePreview() {
    GameHubTheme {
        Settings(uiState = SettingsUiState(
            isLoading = false,
            sections = listOf(
                SettingsSectionUiModel(
                    id = 1, title = "Section 1", items = listOf(
                        SettingsSectionItemUiModel(
                            id = 1,
                            title = "Title 1",
                            description = "Description 1",
                        ),
                        SettingsSectionItemUiModel(
                            id = 2,
                            title = "Title 2",
                            description = "Description 2",
                        ),
                    )
                ), SettingsSectionUiModel(
                    id = 2, title = "Section 2", items = listOf(
                        SettingsSectionItemUiModel(
                            id = 3,
                            title = "Title 1",
                            description = "Description 1",
                        ),
                        SettingsSectionItemUiModel(
                            id = 4,
                            title = "Title 2",
                            description = "Description 2",
                        ),
                        SettingsSectionItemUiModel(
                            id = 5,
                            title = "Title 3",
                            description = "Description 3",
                        ),
                    )
                )
            ),
            selectedThemeName = null,
            isThemePickerVisible = false,
            selectedLanguageName = null,
            isLanguagePickerVisible = false
        ),
            onSettingClicked = {},
            onThemePicked = {},
            onThemePickerDismissed = {},
            onLanguagePicked = {},
            onLanguagePickerDismissed = {})
    }
}
