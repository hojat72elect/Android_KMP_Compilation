

package com.duckduckgo.app.survey.api

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.Call
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface SurveyService {

    @GET("https://staticcdn.duckduckgo.com/survey/v2/survey-mobile.json")
    fun survey(): Call<SurveyGroup?>

    @GET("https://staticcdn.duckduckgo.com/survey/netp/waitlist-survey-mobile.json")
    fun surveyNetPWaitlistBeta(): Call<SurveyGroup?>
}
