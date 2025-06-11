package com.aa.android.pokedex.model

import com.aa.android.pokedex.ui.theme.TypeBug
import com.aa.android.pokedex.ui.theme.TypeDark
import com.aa.android.pokedex.ui.theme.TypeDragon
import com.aa.android.pokedex.ui.theme.TypeElectric
import com.aa.android.pokedex.ui.theme.TypeFairy
import com.aa.android.pokedex.ui.theme.TypeFighting
import com.aa.android.pokedex.ui.theme.TypeFire
import com.aa.android.pokedex.ui.theme.TypeFlying
import com.aa.android.pokedex.ui.theme.TypeGhost
import com.aa.android.pokedex.ui.theme.TypeGrass
import com.aa.android.pokedex.ui.theme.TypeGround
import com.aa.android.pokedex.ui.theme.TypeIce
import com.aa.android.pokedex.ui.theme.TypeNormal
import com.aa.android.pokedex.ui.theme.TypePoison
import com.aa.android.pokedex.ui.theme.TypePsychic
import com.aa.android.pokedex.ui.theme.TypeRock
import com.aa.android.pokedex.ui.theme.TypeShadow
import com.aa.android.pokedex.ui.theme.TypeSteel
import com.aa.android.pokedex.ui.theme.TypeUnknown
import com.aa.android.pokedex.ui.theme.TypeWater
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Just makes sure the background colors we make for each pokemon type are the same as we expect.
 */
class TypeTest {

    @Test
    fun `getColor should return TypeNormal for enum entry Normal`() {
        val sut = Type.getColor("NORMAL")
        assertEquals(TypeNormal, sut)
    }

    @Test
    fun `getColor should return TypeFire for enum entry FIRE`() {
        val sut = Type.getColor("FIRE")
        assertEquals(TypeFire, sut)
    }

    @Test
    fun `getColor should return TypeFighting for enum entry FIGHTING`() {
        val sut = Type.getColor("FIGHTING")
        assertEquals(TypeFighting, sut)
    }

    @Test
    fun `getColor should return TypeWater for enum entry WATER`() {
        val sut = Type.getColor("WATER")
        assertEquals(TypeWater, sut)
    }

    @Test
    fun `getColor should return TypeFlying for enum entry FLYING`() {
        val sut = Type.getColor("FLYING")
        assertEquals(TypeFlying, sut)
    }

    @Test
    fun `getColor should return TypeGrass for enum entry GRASS`() {
        val sut = Type.getColor("GRASS")
        assertEquals(TypeGrass, sut)
    }

    @Test
    fun `getColor should return TypePoison for enum entry POISON`() {
        val sut = Type.getColor("POISON")
        assertEquals(TypePoison, sut)
    }

    @Test
    fun `getColor should return TypeElectric for enum entry ELECTRIC`() {
        val sut = Type.getColor("ELECTRIC")
        assertEquals(TypeElectric, sut)
    }

    @Test
    fun `getColor should return TypeGround for enum entry GROUND`() {
        val sut = Type.getColor("GROUND")
        assertEquals(TypeGround, sut)
    }

    @Test
    fun `getColor should return TypePsychic for enum entry PSYCHIC`() {
        val sut = Type.getColor("PSYCHIC")
        assertEquals(TypePsychic, sut)
    }

    @Test
    fun `getColor should return TypeRock for enum entry ROCK`() {
        val sut = Type.getColor("ROCK")
        assertEquals(TypeRock, sut)
    }

    @Test
    fun `getColor should return TypeIce for enum entry ICE`() {
        val sut = Type.getColor("ICE")
        assertEquals(TypeIce, sut)
    }

    @Test
    fun `getColor should return TypeBug for enum entry BUG`() {
        val sut = Type.getColor("BUG")
        assertEquals(TypeBug, sut)
    }

    @Test
    fun `getColor should return TypeDragon for enum entry DRAGON`() {
        val sut = Type.getColor("DRAGON")
        assertEquals(TypeDragon, sut)
    }

    @Test
    fun `getColor should return TypeGhost for enum entry GHOST`() {
        val sut = Type.getColor("GHOST")
        assertEquals(TypeGhost, sut)
    }

    @Test
    fun `getColor should return TypeDark for enum entry DARK`() {
        val sut = Type.getColor("DARK")
        assertEquals(TypeDark, sut)
    }

    @Test
    fun `getColor should return TypeSteel for enum entry STEEL`() {
        val sut = Type.getColor("STEEL")
        assertEquals(TypeSteel, sut)
    }

    @Test
    fun `getColor should return TypeFairy for enum entry FAIRY`() {
        val sut = Type.getColor("FAIRY")
        assertEquals(TypeFairy, sut)
    }

    @Test
    fun `getColor should return TypeShadow for enum entry SHADOW`() {
        val sut = Type.getColor("SHADOW")
        assertEquals(TypeShadow, sut)
    }

    @Test
    fun `getColor should return TypeUnknown for enum entry UNKNOWN`() {
        val sut = Type.getColor("UNKNOWN")
        assertEquals(TypeUnknown, sut)
    }

}