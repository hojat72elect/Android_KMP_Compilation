

package com.duckduckgo.app.fakes

import com.duckduckgo.user.agent.impl.UserAgent

class UserAgentFake : UserAgent {
    override fun useLegacyUserAgent(url: String): Boolean = false
    override fun isException(url: String): Boolean = false
}
