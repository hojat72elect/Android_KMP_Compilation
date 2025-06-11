package ca.hojat.gamehub.feature_settings.domain.entities

data class Settings(
    val theme: Theme,
    val language: Language
) {
    companion object {
        val DEFAULT = Settings(
            theme = Theme.SYSTEM,
            language = Language.ENGLISH
        )
    }
}
