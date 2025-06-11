

package com.duckduckgo.app.global.rating

import androidx.lifecycle.MutableLiveData

interface AppEnjoymentPromptEmitter {
    val promptType: MutableLiveData<AppEnjoymentPromptOptions>
}

class AppEnjoymentLiveDataEmitter : AppEnjoymentPromptEmitter {

    override val promptType: MutableLiveData<AppEnjoymentPromptOptions> = MutableLiveData()
}
