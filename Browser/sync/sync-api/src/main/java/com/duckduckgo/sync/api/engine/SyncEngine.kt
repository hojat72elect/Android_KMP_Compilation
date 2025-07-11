

package com.duckduckgo.sync.api.engine

interface SyncEngine {

    /**
     * Entry point to the Sync Engine
     * See Tech Design: Sync Updating/Polling Strategy https://app.asana.com/0/481882893211075/1204040479708519/f
     */
    fun triggerSync(trigger: SyncTrigger)

    /**
     * Sync Feature has been disabled / device has been removed
     * This is an opportunity for Features to do some local cleanup if needed
     */
    fun onSyncDisabled()

    /**
     * Represent each possible trigger fo
     * See Tech Design: Sync Engine https://app.asana.com/0/481882893211075/1204303361994831/f
     * [BACKGROUND_SYNC] -> Sync triggered by a Background Worker
     * [APP_OPEN] -> Sync triggered after App is opened
     * [FEATURE_READ] -> Sync triggered when a feature screen is opened (Bookmarks screen, etc...)
     * [DATA_CHANGE] -> Sync triggered because data associated to a Syncable object has changed (new bookmark added)
     * [ACCOUNT_CREATION] -> Sync triggered after creating a new Sync account
     * [ACCOUNT_LOGIN] -> Sync triggered after login into an already existing sync account
     */
    enum class SyncTrigger {
        BACKGROUND_SYNC,
        APP_OPEN,
        FEATURE_READ,
        DATA_CHANGE,
        ACCOUNT_CREATION,
        ACCOUNT_LOGIN,
    }
}
