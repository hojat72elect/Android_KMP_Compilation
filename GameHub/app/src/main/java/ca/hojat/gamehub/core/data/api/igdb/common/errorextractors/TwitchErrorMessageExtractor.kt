package ca.hojat.gamehub.core.data.api.igdb.common.errorextractors

import ca.hojat.gamehub.core.data.api.common.ErrorMessageExtractor
import ca.hojat.gamehub.core.data.api.igdb.common.di.qualifiers.ErrorMessageExtractorKey
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

private const val ERROR_MESSAGE_NAME = "message"

@BindType(withQualifier = true)
@ErrorMessageExtractorKey(ErrorMessageExtractorKey.Type.TWITCH)
class TwitchErrorMessageExtractor @Inject constructor(
    private val json: Json
) : ErrorMessageExtractor {

    override fun extract(responseBody: String): String = try {
        val rootElement = json.parseToJsonElement(responseBody)
        val rootObject = rootElement.jsonObject
        val errorElement = rootObject.getValue(ERROR_MESSAGE_NAME)
        val errorPrimitive = errorElement.jsonPrimitive
        val errorMessage = errorPrimitive.content

        errorMessage
    } catch (expected: Throwable) {
        throw IllegalStateException(
            "Cannot extract a message from the response body: $responseBody",
            expected
        )
    }
}
