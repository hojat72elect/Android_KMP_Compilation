

package com.duckduckgo.app.survey.rmf

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.Action
import com.duckduckgo.remote.messaging.api.JsonActionType.SURVEY
import com.duckduckgo.remote.messaging.api.JsonMessageAction
import com.duckduckgo.remote.messaging.api.MessageActionMapperPlugin
import com.duckduckgo.survey.api.SurveyParameterManager
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@ContributesMultibinding(AppScope::class)
class SurveyActionMapper @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val surveyParameterManager: SurveyParameterManager,
) : MessageActionMapperPlugin {

    override fun evaluate(jsonMessageAction: JsonMessageAction): Action? {
        if (jsonMessageAction.type == SURVEY.jsonValue) {
            val requestedQueryParams = jsonMessageAction.additionalParameters?.get("queryParams")?.split(";") ?: emptyList()

            return runBlocking(dispatcherProvider.io()) {
                if (requestedQueryParams.isEmpty() || surveyParameterManager.buildSurveyUrlStrict(
                        jsonMessageAction.value,
                        requestedQueryParams,
                    ) != null
                ) {
                    Action.Survey(jsonMessageAction.value, jsonMessageAction.additionalParameters)
                } else {
                    null
                }
            }
        }
        return null
    }
}
