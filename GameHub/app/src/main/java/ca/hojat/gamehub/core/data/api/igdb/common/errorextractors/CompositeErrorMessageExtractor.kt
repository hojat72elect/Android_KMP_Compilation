package ca.hojat.gamehub.core.data.api.igdb.common.errorextractors

import ca.hojat.gamehub.core.data.api.common.ErrorMessageExtractor
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.IgdbApi
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

private const val UNKNOWN_ERROR_TEMPLATE = "Unknown Error: %s"

@BindType(withQualifier = true)
@IgdbApi
class CompositeErrorMessageExtractor @Inject constructor(
    private val errorMessageExtractors: Set<@JvmSuppressWildcards ErrorMessageExtractor>
) : ErrorMessageExtractor {

    override fun extract(responseBody: String): String {
        for (errorMessageExtractor in errorMessageExtractors) {
            try {
                val message = errorMessageExtractor.extract(responseBody)

                if (message.isNotBlank()) return message
            } catch (ignore: Throwable) {
                continue
            }
        }

        return String.format(
            UNKNOWN_ERROR_TEMPLATE,
            responseBody
        )
    }
}
