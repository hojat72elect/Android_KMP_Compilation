

package com.duckduckgo.sync.impl.pixels

import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SyncPixelParamRemovalPlugin @Inject constructor() : PixelParamRemovalPlugin {
    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            SyncPixelName.SYNC_PATCH_COMPRESS_FAILED.pixelName to PixelParameter.removeAtb(),

            SyncPixelName.SYNC_FEATURE_PROMOTION_DISPLAYED.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_FEATURE_PROMOTION_CONFIRMED.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_FEATURE_PROMOTION_DISMISSED.pixelName to PixelParameter.removeAtb(),

            SyncPixelName.SYNC_GET_OTHER_DEVICES_SCREEN_SHOWN.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_GET_OTHER_DEVICES_LINK_COPIED.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_GET_OTHER_DEVICES_LINK_SHARED.pixelName to PixelParameter.removeAtb(),

            SyncPixelName.SYNC_ASK_USER_TO_SWITCH_ACCOUNT.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_USER_ACCEPTED_SWITCHING_ACCOUNT.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_USER_CANCELLED_SWITCHING_ACCOUNT.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_USER_SWITCHED_ACCOUNT.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_USER_SWITCHED_LOGOUT_ERROR.pixelName to PixelParameter.removeAtb(),
            SyncPixelName.SYNC_USER_SWITCHED_LOGIN_ERROR.pixelName to PixelParameter.removeAtb(),
        )
    }
}
