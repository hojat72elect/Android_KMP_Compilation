

package com.duckduckgo.duckplayer.impl

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.ui.view.listitem.OneLineListItem
import com.duckduckgo.common.utils.extensions.toBinaryString
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.duckplayer.api.DuckPlayer
import com.duckduckgo.duckplayer.api.DuckPlayerSettingsNoParams
import com.duckduckgo.duckplayer.impl.DuckPlayerPixelName.DUCK_PLAYER_SETTINGS_PRESSED
import com.duckduckgo.mobile.android.R as CommonR
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.settings.api.DuckPlayerSettingsPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(ActivityScope::class)
@PriorityKey(100)
class DuckPlayerSettingsTitle @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
    private val pixel: Pixel,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val duckPlayer: DuckPlayer,
) : DuckPlayerSettingsPlugin {
    override fun getView(context: Context): View {
        return OneLineListItem(context).apply {
            setLeadingIconResource(CommonR.drawable.ic_video_player_color_24)

            setPrimaryText(context.getString(R.string.duck_player_setting_title))
            setOnClickListener {
                globalActivityStarter.start(this.context, DuckPlayerSettingsNoParams, null)
                appCoroutineScope.launch {
                    val wasUsedBefore = duckPlayer.wasUsedBefore()
                    pixel.fire(DUCK_PLAYER_SETTINGS_PRESSED, parameters = mapOf("was_used_before" to wasUsedBefore.toBinaryString()))
                }
            }
        }
    }
}
