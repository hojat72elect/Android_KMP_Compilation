package ca.hojat.gamehub.core.domain.entities

enum class AgeRatingCategory(val title: String) {
    UNKNOWN(title = ""),
    ESRB(title = "ESRB"),
    PEGI(title = "PEGI"),
}
