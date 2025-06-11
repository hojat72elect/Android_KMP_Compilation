package ca.hojat.gamehub.core.data.api.gamespot.common

import ca.hojat.gamehub.core.data.api.common.ErrorMessageExtractor
import ca.hojat.gamehub.core.data.api.gamespot.common.di.GamespotApi
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

private const val ERROR_MESSAGE_NAME = "error"

@BindType(withQualifier = true)
@GamespotApi
class GamespotErrorMessageExtractor @Inject constructor(
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
        (expected.message ?: expected.javaClass.simpleName)
    }
}
