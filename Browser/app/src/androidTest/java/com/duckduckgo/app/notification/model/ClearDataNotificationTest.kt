

@file:Suppress("RemoveExplicitTypeArguments")

package com.duckduckgo.app.notification.model

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.notification.db.NotificationDao
import com.duckduckgo.app.settings.clear.ClearWhatOption
import com.duckduckgo.app.settings.db.SettingsDataStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ClearDataNotificationTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val notificationsDao: NotificationDao = mock()
    private val settingsDataStore: SettingsDataStore = mock()

    private lateinit var testee: ClearDataNotification

    @Before
    fun before() {
        testee = ClearDataNotification(context, notificationsDao, settingsDataStore)
    }

    @Test
    fun whenNotificationNotSeenAndOptionNotSetThenCanShowIsTrue() = runTest {
        whenever(notificationsDao.exists(any())).thenReturn(false)
        whenever(settingsDataStore.automaticallyClearWhatOption).thenReturn(ClearWhatOption.CLEAR_NONE)
        assertTrue(testee.canShow())
    }

    @Test
    fun whenNotificationNotSeenButOptionAlreadySetThenCanShowIsFalse() = runTest {
        whenever(notificationsDao.exists(any())).thenReturn(false)
        whenever(settingsDataStore.automaticallyClearWhatOption).thenReturn(ClearWhatOption.CLEAR_TABS_ONLY)
        assertFalse(testee.canShow())
    }

    @Test
    fun whenNotificationAlreadySeenAndOptionNotSetThenCanShowIsFalse() = runTest {
        whenever(notificationsDao.exists(any())).thenReturn(true)
        whenever(settingsDataStore.automaticallyClearWhatOption).thenReturn(ClearWhatOption.CLEAR_NONE)
        assertFalse(testee.canShow())
    }
}
