

package com.duckduckgo.voice.store

import com.duckduckgo.voice.api.VoiceSearchStatusListener

interface VoiceSearchRepository {
    fun declinePermissionForever()
    fun acceptRationaleDialog()
    fun saveLoggedAvailability()
    fun getHasPermissionDeclinedForever(): Boolean
    fun getHasAcceptedRationaleDialog(): Boolean
    fun getHasLoggedAvailability(): Boolean
    fun isVoiceSearchUserEnabled(default: Boolean): Boolean
    fun setVoiceSearchUserEnabled(enabled: Boolean)
    fun countVoiceSearchDismissed(): Int
    fun dismissVoiceSearch()
    fun resetVoiceSearchDismissed()
}

class RealVoiceSearchRepository constructor(
    private val dataStore: VoiceSearchDataStore,
    private val voiceSearchStatusListener: VoiceSearchStatusListener,
) : VoiceSearchRepository {
    override fun declinePermissionForever() {
        dataStore.permissionDeclinedForever = true
    }

    override fun acceptRationaleDialog() {
        dataStore.userAcceptedRationaleDialog = true
    }

    override fun saveLoggedAvailability() {
        dataStore.availabilityLogged = true
    }

    override fun getHasPermissionDeclinedForever(): Boolean = dataStore.permissionDeclinedForever

    override fun getHasAcceptedRationaleDialog(): Boolean = dataStore.userAcceptedRationaleDialog

    override fun getHasLoggedAvailability(): Boolean = dataStore.availabilityLogged

    override fun isVoiceSearchUserEnabled(default: Boolean): Boolean = dataStore.isVoiceSearchEnabled(default)

    override fun setVoiceSearchUserEnabled(enabled: Boolean) {
        dataStore.setVoiceSearchEnabled(enabled)
        voiceSearchStatusListener.voiceSearchStatusChanged()
    }

    override fun countVoiceSearchDismissed(): Int {
        return dataStore.countVoiceSearchDismissed
    }

    override fun dismissVoiceSearch() {
        dataStore.countVoiceSearchDismissed = dataStore.countVoiceSearchDismissed + 1
    }

    override fun resetVoiceSearchDismissed() {
        dataStore.countVoiceSearchDismissed = 0
    }
}
