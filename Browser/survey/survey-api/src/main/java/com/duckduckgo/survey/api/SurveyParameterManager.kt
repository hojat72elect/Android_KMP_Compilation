

package com.duckduckgo.survey.api

interface SurveyParameterManager {
    /**
     * Builds the survey url, resolving the values for the ALL the required query parameters
     *
     * @return resolved survey url or `null` if not all query parameters can be provided
     */
    suspend fun buildSurveyUrlStrict(baseUrl: String, requestedQueryParams: List<String>): String?

    /**
     * Builds the survey url regardless if all required query parameters can be resolved
     *
     * @return resolved survey url where if a parameter could not be provided, it is set to empty string instead
     */
    suspend fun buildSurveyUrl(baseUrl: String, requestedQueryParams: List<String>): String
}
