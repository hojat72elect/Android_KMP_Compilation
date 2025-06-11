package com.aa.android.pokedex.model

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import com.aa.android.pokedex.R
import com.aa.android.pokedex.ui.theme.*

/**
 * Enum class representing the different Pokémon types and their associated colors.
 *
 * @param color Color definition for Compose UI
 * @param colorInt Color resource for traditional UI
 */
enum class Type(val color: Color, @ColorInt val colorInt: Int) {
    NORMAL(TypeNormal, R.color.typeNormal),
    FIRE(TypeFire, R.color.typeFire),
    FIGHTING(TypeFighting, R.color.typeFighting),
    WATER(TypeWater, R.color.typeWater),
    FLYING(TypeFlying, R.color.typeFlying),
    GRASS(TypeGrass, R.color.typeGrass),
    POISON(TypePoison, R.color.typePoison),
    ELECTRIC(TypeElectric, R.color.typeElectric),
    GROUND(TypeGround, R.color.typeGround),
    PSYCHIC(TypePsychic, R.color.typePsychic),
    ROCK(TypeRock, R.color.typeRock),
    ICE(TypeIce, R.color.typeIce),
    BUG(TypeBug, R.color.typeBug),
    DRAGON(TypeDragon, R.color.typeDragon),
    GHOST(TypeGhost, R.color.typeGhost),
    DARK(TypeDark, R.color.typeDark),
    STEEL(TypeSteel, R.color.typeSteel),
    FAIRY(TypeFairy, R.color.typeFairy),
    SHADOW(TypeShadow, R.color.typeShadow),
    UNKNOWN(TypeUnknown, R.color.typeUnknown);

    companion object {
        /**
         * Get the [Color] associated with the supplied Type name.
         *
         * @param name The name of the type, e.g. "normal" or "fire".
         */
        fun getColor(name: String): Color {
            return valueOf(name).color
        }

        /**
         * Get the [ColorInt] resource associated with the supplied Type name.
         *
         * @param name The name of the type, e.g. "normal" or "fire".
         */
        @ColorInt
        fun getColorInt(name: String): Int {
            return valueOf(name).colorInt
        }
    }
}