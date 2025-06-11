@file:JvmName("StringUtils")

package ca.hojat.gamehub.core.extensions

private const val SEPARATOR = ","

fun String.fromCsv(): List<String> {
    return split(SEPARATOR)
}
