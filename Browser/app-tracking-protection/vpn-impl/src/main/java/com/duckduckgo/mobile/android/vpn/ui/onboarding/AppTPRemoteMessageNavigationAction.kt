

package com.duckduckgo.mobile.android.vpn.ui.onboarding

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.Action
import com.duckduckgo.remote.messaging.api.JsonActionType.APP_TP_ONBOARDING
import com.duckduckgo.remote.messaging.api.JsonMessageAction
import com.duckduckgo.remote.messaging.api.MessageActionMapperPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.*

@ContributesMultibinding(
    AppScope::class,
    boundType = MessageActionMapperPlugin::class,
)
class AppTPRemoteMessageNavigationAction @Inject constructor() : MessageActionMapperPlugin {

    override fun evaluate(jsonMessageAction: JsonMessageAction): Action? {
        val isAppTPOnboarding = jsonMessageAction.type == APP_TP_ONBOARDING.jsonValue
        return if (isAppTPOnboarding) {
            Action.AppTpOnboarding
        } else {
            null
        }
    }
}
