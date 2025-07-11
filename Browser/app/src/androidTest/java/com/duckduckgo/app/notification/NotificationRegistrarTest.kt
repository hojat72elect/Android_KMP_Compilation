

package com.duckduckgo.app.notification

import androidx.core.app.NotificationManagerCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.notification.model.NotificationPlugin
import com.duckduckgo.app.notification.model.SchedulableNotificationPlugin
import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.app.statistics.pixels.Pixel.PixelType.Count
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.test.CoroutineTestRule
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.experiments.api.VariantManager
import kotlinx.coroutines.test.TestScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class NotificationRegistrarTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val notificationManagerCompat = NotificationManagerCompat.from(context)

    private val mockSettingsDataStore: SettingsDataStore = mock()
    private val mockVariantManager: VariantManager = mock()
    private val mockPixel: Pixel = mock()
    private val mockSchedulableNotificationPluginPoint: PluginPoint<SchedulableNotificationPlugin> = mock()
    private val mockNotificationPluginPoint: PluginPoint<NotificationPlugin> = mock()
    private val appBuildConfig: AppBuildConfig = mock()

    private lateinit var testee: NotificationRegistrar

    @Before
    fun before() {
        whenever(mockVariantManager.getVariantKey()).thenReturn("DEFAULT_VARIANT")
        whenever(appBuildConfig.sdkInt).thenReturn(30)
        testee = NotificationRegistrar(
            TestScope(),
            context,
            notificationManagerCompat,
            mockSettingsDataStore,
            mockPixel,
            mockSchedulableNotificationPluginPoint,
            mockNotificationPluginPoint,
            appBuildConfig,
            coroutineRule.testDispatcherProvider,
        )
    }

    @Test
    fun whenNotificationsPreviouslyOffAndNowOnThenPixelIsFiredAndSettingsUpdated() {
        whenever(mockSettingsDataStore.appNotificationsEnabled).thenReturn(false)
        testee.updateStatus(true)
        verify(mockPixel).fire(eq(AppPixelName.NOTIFICATIONS_ENABLED), any(), any(), eq(Count))
        verify(mockSettingsDataStore).appNotificationsEnabled = true
    }

    @Test
    fun whenNotificationsPreviouslyOffAndStillOffThenNoPixelIsFiredAndSettingsUnchanged() {
        whenever(mockSettingsDataStore.appNotificationsEnabled).thenReturn(false)
        testee.updateStatus(false)
        verify(mockPixel, never()).fire(any<Pixel.PixelName>(), any(), any(), eq(Count))
        verify(mockSettingsDataStore, never()).appNotificationsEnabled = true
    }

    @Test
    fun whenNotificationsPreviouslyOnAndStillOnThenNoPixelIsFiredAndSettingsUnchanged() {
        whenever(mockSettingsDataStore.appNotificationsEnabled).thenReturn(true)
        testee.updateStatus(true)
        verify(mockPixel, never()).fire(any<Pixel.PixelName>(), any(), any(), eq(Count))
        verify(mockSettingsDataStore, never()).appNotificationsEnabled = false
    }

    @Test
    fun whenNotificationsPreviouslyOnAndNowOffPixelIsFiredAndSettingsUpdated() {
        whenever(mockSettingsDataStore.appNotificationsEnabled).thenReturn(true)
        testee.updateStatus(false)
        verify(mockPixel).fire(eq(AppPixelName.NOTIFICATIONS_DISABLED), any(), any(), eq(Count))
        verify(mockSettingsDataStore).appNotificationsEnabled = false
    }
}
