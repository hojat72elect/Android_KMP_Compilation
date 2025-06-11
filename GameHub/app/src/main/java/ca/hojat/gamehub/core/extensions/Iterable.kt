package ca.hojat.gamehub.core.extensions

private const val SEPARATOR = ","

fun Iterable<String>.toCsv(): String {
    return joinToString(separator = SEPARATOR)
}
