package ca.hojat.gamehub.feature_settings.domain.entities

import ca.hojat.gamehub.R

enum class Theme(val uiTextRes: Int) {
    LIGHT(R.string.settings_item_theme_option_light),
    DARK(R.string.settings_item_theme_option_dark),
    SYSTEM(R.string.settings_item_theme_option_system_default),

}
