

package com.duckduckgo.app.browser.mediaplayback

import androidx.core.net.toUri
import com.duckduckgo.app.browser.mediaplayback.store.MediaPlaybackRepository
import com.duckduckgo.common.utils.baseHost
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface MediaPlayback {
    fun doesMediaPlaybackRequireUserGestureForUrl(url: String): Boolean
}

@ContributesBinding(AppScope::class)
class RealMediaPlayback @Inject constructor(
    private val mediaPlaybackFeature: MediaPlaybackFeature,
    private val mediaPlaybackRepository: MediaPlaybackRepository,
) : MediaPlayback {

    override fun doesMediaPlaybackRequireUserGestureForUrl(url: String): Boolean {
        val uri = url.toUri()
        return mediaPlaybackFeature.self().isEnabled() && mediaPlaybackRepository.exceptions.none { it.domain == uri.baseHost }
    }
}
