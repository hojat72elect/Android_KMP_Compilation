package ca.hojat.gamehub.feature_settings.domain.datastores

import ca.hojat.gamehub.feature_settings.domain.entities.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {
    suspend fun saveSettings(settings: Settings)
    fun observeSettings(): Flow<Settings>
}
