package ca.hojat.gamehub.core.domain.entities

enum class AgeRatingType(val title: String) {
    UNKNOWN(title = ""),
    THREE(title = "3"),
    SEVEN(title = "7"),
    TWELVE(title = "12"),
    SIXTEEN(title = "16"),
    EIGHTEEN(title = "18"),
    RP(title = "RP"),
    EC(title = "EC"),
    E(title = "E"),
    E10(title = "E10"),
    T(title = "T"),
    M(title = "M"),
    AO(title = "AO"),
}
