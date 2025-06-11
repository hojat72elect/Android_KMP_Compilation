

package com.duckduckgo.autofill.impl.configuration

import com.duckduckgo.autofill.impl.jsbridge.response.AvailableInputSuccessResponse
import com.duckduckgo.autofill.impl.jsbridge.response.AvailableInputTypeCredentials
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.Moshi
import dagger.SingleInstanceIn
import javax.inject.Inject

interface RuntimeConfigurationWriter {
    fun generateResponseGetAvailableInputTypes(
        credentialsAvailable: AvailableInputTypeCredentials,
        emailAvailable: Boolean,
    ): String

    fun generateContentScope(settingsJson: AutofillSiteSpecificFixesSettings): String
    fun generateUserUnprotectedDomains(): String
    fun generateUserPreferences(
        autofillCredentials: Boolean,
        credentialSaving: Boolean,
        passwordGeneration: Boolean,
        showInlineKeyIcon: Boolean,
        showInContextEmailProtectionSignup: Boolean,
        unknownUsernameCategorization: Boolean,
        partialFormSaves: Boolean,
    ): String
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealRuntimeConfigurationWriter @Inject constructor(val moshi: Moshi) : RuntimeConfigurationWriter {
    private val availableInputTypesAdapter = moshi.adapter(AvailableInputSuccessResponse::class.java).indent("  ")

    override fun generateResponseGetAvailableInputTypes(
        credentialsAvailable: AvailableInputTypeCredentials,
        emailAvailable: Boolean,
    ): String {
        val availableInputTypes = AvailableInputSuccessResponse(credentialsAvailable, emailAvailable)
        return availableInputTypesAdapter.toJson(availableInputTypes)
    }

    private fun generateSiteSpecificFixesJson(settingsJson: AutofillSiteSpecificFixesSettings): String {
        if (!settingsJson.canApplySiteSpecificFixes) {
            return ""
        }

        return """
            "siteSpecificFixes": {
                "state": "enabled",
                "settings": ${settingsJson.javascriptConfigSiteSpecificFixes}
            }
        """.trimIndent()
    }

    override fun generateContentScope(settingsJson: AutofillSiteSpecificFixesSettings): String {
        return """
            contentScope = {
              "features": {
                "autofill": {
                  "state": "enabled",
                  "exceptions": [],
                  "features": {
                    ${generateSiteSpecificFixesJson(settingsJson)}
                  }
                }
              },
              "unprotectedTemporary": []
            };
        """.trim()
    }

    /*
     * userUnprotectedDomains: any sites for which the user has chosen to disable privacy protections (leave empty for now)
     */
    override fun generateUserUnprotectedDomains(): String {
        return """
            userUnprotectedDomains = [];
        """.trimIndent()
    }

    override fun generateUserPreferences(
        autofillCredentials: Boolean,
        credentialSaving: Boolean,
        passwordGeneration: Boolean,
        showInlineKeyIcon: Boolean,
        showInContextEmailProtectionSignup: Boolean,
        unknownUsernameCategorization: Boolean,
        partialFormSaves: Boolean,
    ): String {
        return """
            userPreferences = {
              "debug": false,
              "platform": {
                "name": "android"
              },
              "features": {
                "autofill": {
                  "settings": {
                    "featureToggles": {
                      "inputType_credentials": $autofillCredentials,
                      "inputType_identities": false,
                      "inputType_creditCards": false,
                      "emailProtection": true,
                      "password_generation": $passwordGeneration,
                      "credentials_saving": $credentialSaving,
                      "inlineIcon_credentials": $showInlineKeyIcon,
                      "emailProtection_incontext_signup": $showInContextEmailProtectionSignup,
                      "unknown_username_categorization": $unknownUsernameCategorization,
                      "partial_form_saves": $partialFormSaves
                    }
                  }
                }
              }
            };
        """.trimIndent()
    }
}
