

package com.duckduckgo.subscriptions.impl.feedback.pixels

import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface PrivacyProUnifiedFeedbackPixelSender {
    fun sendPproFeatureRequest(parameters: Map<String, String>)
    fun sendPproGeneralFeedback(parameters: Map<String, String>)
    fun sendPproReportIssue(parameters: Map<String, String>)
    fun reportPproFeedbackGeneralScreenShown()
    fun reportPproFeedbackActionsScreenShown(parameters: Map<String, String>)
    fun reportPproFeedbackCategoryScreenShown(parameters: Map<String, String>)
    fun reportPproFeedbackSubcategoryScreenShown(parameters: Map<String, String>)
    fun reportPproFeedbackSubmitScreenShown(parameters: Map<String, String>)
    fun reportPproFeedbackSubmitScreenFaqClicked(parameters: Map<String, String>)
}

@ContributesBinding(AppScope::class)
class RealPrivacyProUnifiedFeedbackPixelSender @Inject constructor(
    private val pixelSender: Pixel,
) : PrivacyProUnifiedFeedbackPixelSender {
    override fun sendPproFeatureRequest(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.PPRO_FEEDBACK_FEATURE_REQUEST, parameters)
    }

    override fun sendPproGeneralFeedback(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.PPRO_FEEDBACK_GENERAL_FEEDBACK, parameters)
    }

    override fun sendPproReportIssue(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.PPRO_FEEDBACK_REPORT_ISSUE, parameters)
    }

    override fun reportPproFeedbackGeneralScreenShown() {
        fire(PrivacyProUnifiedFeedbackPixel.IMPRESSION_PPRO_FEEDBACK_GENERAL_SCREEN)
    }

    override fun reportPproFeedbackActionsScreenShown(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.IMPRESSION_PPRO_FEEDBACK_ACTION_SCREEN, parameters)
    }

    override fun reportPproFeedbackCategoryScreenShown(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.IMPRESSION_PPRO_FEEDBACK_CATEGORY_SCREEN, parameters)
    }

    override fun reportPproFeedbackSubcategoryScreenShown(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.IMPRESSION_PPRO_FEEDBACK_SUBCATEGORY_SCREEN, parameters)
    }

    override fun reportPproFeedbackSubmitScreenShown(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.IMPRESSION_PPRO_FEEDBACK_SUBMIT_SCREEN, parameters)
    }

    override fun reportPproFeedbackSubmitScreenFaqClicked(parameters: Map<String, String>) {
        fire(PrivacyProUnifiedFeedbackPixel.PPRO_FEEDBACK_SUBMIT_SCREEN_FAQ_CLICK, parameters)
    }

    private fun fire(
        pixel: PrivacyProUnifiedFeedbackPixel,
        params: Map<String, String> = emptyMap(),
    ) {
        pixel.getPixelNames().forEach { (pixelType, pixelName) ->
            pixelSender.fire(pixelName = pixelName, type = pixelType, parameters = params)
        }
    }
}
