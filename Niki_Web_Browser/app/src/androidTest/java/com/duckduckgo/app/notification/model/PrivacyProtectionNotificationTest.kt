

@file:Suppress("RemoveExplicitTypeArguments")

package com.duckduckgo.app.notification.model

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.notification.db.NotificationDao
import com.duckduckgo.app.privacy.db.PrivacyProtectionCountDao
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PrivacyProtectionNotificationTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val notificationsDao: NotificationDao = mock()
    private val privactCountDao: PrivacyProtectionCountDao = mock()

    private lateinit var testee: PrivacyProtectionNotification

    @Before
    fun before() {
        testee = PrivacyProtectionNotification(context, notificationsDao, privactCountDao)
    }

    @Test
    fun whenNotificationNotSeenThenCanShowIsTrue() = runTest {
        whenever(notificationsDao.exists(any())).thenReturn(false)
        assertTrue(testee.canShow())
    }

    @Test
    fun whenNotificationAlreadySeenThenCanShowIsFalse() = runTest {
        whenever(notificationsDao.exists(any())).thenReturn(true)
        assertFalse(testee.canShow())
    }
}
