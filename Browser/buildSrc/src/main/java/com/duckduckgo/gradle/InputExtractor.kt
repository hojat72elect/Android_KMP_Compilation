

package com.duckduckgo.gradle

import org.gradle.api.GradleException

class InputExtractor {

    fun extractFeatureNameAndTypes(input: String): Pair<String, ModuleType> {
        validateFeatureName(input)
        val (featureName, moduleType) = input.split("/")
        validateModuleType(moduleType)
        return Pair(featureName, ModuleType.moduleTypeFromInput(moduleType)).also {
            println("""Feature is [$featureName] and module type is [${moduleType}]""")
        }
    }

    private fun validateFeatureName(featureName: String) {
        if (featureName.isEmpty()) throw GradleException(ERROR_MESSAGE_EMPTY_NAME)
        if (!featureName.matches(ACCEPTABLE_CHARACTERS_REGEX)) throw GradleException(ERROR_MESSAGE_INVALID_CHARS)
        if (featureName.count { it == '/' } != 1) throw GradleException(ERROR_MESSAGE_UNEXPECTED_NUMBER_OF_FORWARD_SLASHES)
        if (featureName.startsWith("/")) throw GradleException(ERROR_MESSAGE_STARTS_WITH_FORWARD_SLASH)
    }

    private fun validateModuleType(moduleType: String) {
        if (!moduleType.matches(VALID_MODULE_TYPES_REGEX)) {
            throw GradleException("Invalid module type [$moduleType]. Must be one of [ ${ModuleType.validInputTypes().joinToString(" | ")} ]")
        }
    }

    companion object {
        private val VALID_MODULE_TYPES_REGEX = "^(${ModuleType.validInputTypes().joinToString("|")})$".toRegex()
        private val ACCEPTABLE_CHARACTERS_REGEX = "^[a-z0-9-/]*$".toRegex()

        private const val ERROR_MESSAGE_EMPTY_NAME = "Feature name cannot be empty"
        private const val ERROR_MESSAGE_UNEXPECTED_NUMBER_OF_FORWARD_SLASHES = "Feature name must contain exactly one forward slash"
        private const val ERROR_MESSAGE_STARTS_WITH_FORWARD_SLASH = "Feature name cannot start with a forward slash"
        private const val ERROR_MESSAGE_INVALID_CHARS =
            "Feature name can only contain lowercase letters, numbers, dashes and one forward slash"
    }
}
