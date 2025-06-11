

package com.duckduckgo.subscriptions.impl.pixels

import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.EnumSet
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SubscriptionPixelParamRemovalPlugin @Inject constructor() : PixelParamRemovalPlugin {
    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return SubscriptionPixel.entries
            .map { pixel -> pixel.baseName to EnumSet.complementOf(EnumSet.copyOf(pixel.includedParameters)) }
            .filter { (_, removedParameters) -> removedParameters.isNotEmpty() }
            .plus("m_ppro_feedback" to PixelParameter.removeAtb())
    }
}
