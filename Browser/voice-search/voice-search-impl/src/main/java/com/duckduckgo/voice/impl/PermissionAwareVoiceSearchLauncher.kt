

package com.duckduckgo.voice.impl

import android.app.Activity
import androidx.activity.result.ActivityResultCaller
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.voice.api.VoiceSearchAvailability
import com.duckduckgo.voice.api.VoiceSearchLauncher
import com.duckduckgo.voice.api.VoiceSearchLauncher.Event
import com.duckduckgo.voice.api.VoiceSearchLauncher.Event.VoiceSearchDisabled
import com.duckduckgo.voice.api.VoiceSearchLauncher.Source
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(ActivityScope::class)
class PermissionAwareVoiceSearchLauncher @Inject constructor(
    private val permissionRequest: PermissionRequest,
    private val voiceSearchActivityLauncher: VoiceSearchActivityLauncher,
    private val voiceSearchPermissionCheck: VoiceSearchPermissionCheck,
    private val voiceSearchAvailability: VoiceSearchAvailability,
) : VoiceSearchLauncher {

    override fun registerResultsCallback(
        caller: ActivityResultCaller,
        activity: Activity,
        source: Source,
        onEvent: (Event) -> Unit,
    ) {
        permissionRequest.registerResultsCallback(
            caller,
            activity,
            { voiceSearchActivityLauncher.launch(activity) },
            { onEvent(VoiceSearchDisabled) },
        )
        voiceSearchActivityLauncher.registerResultsCallback(caller, activity, source) {
            onEvent(it)
        }
    }

    override fun launch(activity: Activity) {
        if (!voiceSearchAvailability.isVoiceSearchAvailable) return

        if (voiceSearchPermissionCheck.hasRequiredPermissionsGranted()) {
            voiceSearchActivityLauncher.launch(activity)
        } else {
            permissionRequest.launch(activity)
        }
    }
}
