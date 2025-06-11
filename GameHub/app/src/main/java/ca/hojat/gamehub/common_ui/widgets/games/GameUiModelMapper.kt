package ca.hojat.gamehub.common_ui.widgets.games

import ca.hojat.gamehub.core.factories.IgdbImageSize
import ca.hojat.gamehub.core.factories.IgdbImageUrlFactory
import ca.hojat.gamehub.core.formatters.GameReleaseDateFormatter
import ca.hojat.gamehub.core.domain.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface GameUiModelMapper {
    fun mapToUiModel(game: Game): GameUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
class GameUiModelMapperImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory,
    private val gameReleaseDateFormatter: GameReleaseDateFormatter
) : GameUiModelMapper {

    override fun mapToUiModel(game: Game): GameUiModel {
        return GameUiModel(
            id = game.id,
            coverImageUrl = game.buildCoverImageUrl(),
            name = game.name,
            releaseDate = gameReleaseDateFormatter.formatReleaseDate(game),
            developerName = game.developerCompany?.name,
            description = game.buildDescription(),
        )
    }

    private fun Game.buildCoverImageUrl(): String? {
        return cover?.let { cover ->
            igdbImageUrlFactory.createUrl(
                cover,
                IgdbImageUrlFactory.Config(IgdbImageSize.BIG_COVER)
            )
        }
    }

    private fun Game.buildDescription(): String? {
        if (summary != null) return summary
        if (storyline != null) return storyline

        return null
    }
}

fun GameUiModelMapper.mapToUiModels(games: List<Game>): List<GameUiModel> {
    return games.map(::mapToUiModel)
}
