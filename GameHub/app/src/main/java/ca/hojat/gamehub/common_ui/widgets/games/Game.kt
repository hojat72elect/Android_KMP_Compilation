package ca.hojat.gamehub.common_ui.widgets.games

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import ca.hojat.gamehub.common_ui.widgets.DefaultCoverHeight
import ca.hojat.gamehub.common_ui.widgets.GameCover
import ca.hojat.gamehub.common_ui.widgets.GameHubCard
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import kotlin.math.roundToInt

@Composable
fun Game(
    game: GameUiModel,
    onClick: () -> Unit,
) {
    GameHubCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameHubTheme.spaces.spacing_3_5)
        ) {
            GameCover(
                title = null,
                imageUrl = game.coverImageUrl,
            )

            Details(
                name = game.name,
                releaseDate = game.releaseDate,
                developerName = game.developerName,
                description = game.description,
                modifier = Modifier.height(DefaultCoverHeight),
            )
        }
    }
}

@Composable
private fun Details(
    name: String,
    releaseDate: String,
    developerName: String?,
    description: String?,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            modifier = Modifier.padding(start = GameHubTheme.spaces.spacing_3_0),
            color = GameHubTheme.colors.onPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = GameHubTheme.typography.subtitle2,
        )

        Text(
            text = releaseDate,
            modifier = Modifier
                .padding(
                    top = GameHubTheme.spaces.spacing_2_5,
                    start = GameHubTheme.spaces.spacing_3_0,
                ),
            style = GameHubTheme.typography.caption,
        )

        if (developerName != null) {
            Text(
                text = developerName,
                modifier = Modifier.padding(start = GameHubTheme.spaces.spacing_3_0),
                style = GameHubTheme.typography.caption,
            )
        }

        if (description != null) {
            DetailsDescription(description = description)
        }
    }
}

@Composable
private fun DetailsDescription(description: String) {
    var maxLines by rememberSaveable { mutableStateOf(Int.MAX_VALUE) }

    Text(
        text = description,
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                top = GameHubTheme.spaces.spacing_2_5,
                start = GameHubTheme.spaces.spacing_3_0,
            ),
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.multiParagraph.lineCount > 0) {
                val textHeight = textLayoutResult.size.height
                val firstLineHeight = textLayoutResult.multiParagraph.getLineHeight(0)

                (textHeight / firstLineHeight).roundToInt().let { calculatedMaxLines ->
                    if (calculatedMaxLines > 0)
                        maxLines = calculatedMaxLines
                }

            }
        },
        style = GameHubTheme.typography.body2.copy(
            lineHeight = TextUnit.Unspecified,
        ),
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameFullPreview() {
    GameHubTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = "Playground Games",
                description = "Your Ultimate Horizon Adventure awaits! Explore the vibrant " +
                        "and ever-evolving open-world landscapes of Mexico.",
            ),
            onClick = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameWithoutDeveloperPreview() {
    GameHubTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = null,
                description = "Your Ultimate Horizon Adventure awaits! Explore the vibrant " +
                        "and ever-evolving open-world landscapes of Mexico.",
            ),
            onClick = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameWithoutDescriptionPreview() {
    GameHubTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = "Playground Games",
                description = null,
            ),
            onClick = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GameMinimalPreview() {
    GameHubTheme {
        Game(
            game = GameUiModel(
                id = 1,
                coverImageUrl = null,
                name = "Forza Horizon 5",
                releaseDate = "Nov 09, 2021 (7 days ago)",
                developerName = null,
                description = null,
            ),
            onClick = {},
        )
    }
}
