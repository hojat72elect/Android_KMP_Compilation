

package com.wireguard.crypto

infix fun Byte.and(that: Int): Int = this.toInt().and(that)

infix fun Byte.or(that: Int): Int = this.toInt().or(that)

infix fun Byte.shr(that: Int): Int = this.toInt().shr(that)

infix fun Byte.shl(that: Int): Int = this.toInt().shl(that)

infix fun Byte.ushr(that: Int): Int = this.toInt().ushr(that)
