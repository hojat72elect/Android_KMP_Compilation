

package com.duckduckgo.app.survey.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface SurveyEndpointDataStore {
    var useSurveyCustomEnvironmentUrl: Boolean
}

@ContributesBinding(AppScope::class)
class SurveyEndpointDataStoreImpl @Inject constructor(
    context: Context,
) : SurveyEndpointDataStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override var useSurveyCustomEnvironmentUrl: Boolean
        get() = preferences.getBoolean(KEY_SURVEY_USE_ENVIRONMENT_URL, false)
        set(enabled) = preferences.edit { putBoolean(KEY_SURVEY_USE_ENVIRONMENT_URL, enabled) }

    companion object {
        private const val FILENAME = "com.duckduckgo.app.survey.api.survey.endpoint.v1"
        private const val KEY_SURVEY_USE_ENVIRONMENT_URL = "KEY_SURVEY_ENVIRONMENT_URL"
    }
}
