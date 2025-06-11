

@file:Suppress("RemoveExplicitTypeArguments", "SameParameterValue")

package com.duckduckgo.app.browser.tabpreview

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.global.file.FileDeleter
import com.duckduckgo.common.test.CoroutineTestRule
import java.io.File
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class FileBasedWebViewPreviewPersisterTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private lateinit var testee: FileBasedWebViewPreviewPersister
    private val mockFileDeleter: FileDeleter = mock()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        testee = FileBasedWebViewPreviewPersister(context, mockFileDeleter, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun whenFullPathReturnedForPreviewFileThenCacheDirectoryUsed() {
        val tabId = "ABC-123"
        val previewFileName = "12345.jpg"
        val path = testee.fullPathForFile(tabId, previewFileName)
        verifyCacheDirectoryUsed(path)
    }

    @Test
    fun whenFullPathReturnedForPreviewFileThenSpecificTabPreviewDirectoryUsed() {
        val tabId = "ABC-123"
        val previewFileName = "12345.jpg"
        val path = testee.fullPathForFile(tabId, previewFileName)
        verifyTabPreviewDirectoryUse(path)
    }

    @Test
    fun whenFullPathReturnedForPreviewFileThenTabIdUsedAsParentDirectory() {
        val tabId = "ABC-123"
        val previewFileName = "12345.jpg"
        val path = File(testee.fullPathForFile(tabId, previewFileName))
        verifyTabIdUsedAsDirectory(tabId, path)
    }

    @Test
    fun whenDeleteAllCalledThenEntireTabPreviewDirectoryDeleted() = runTest {
        testee.deleteAll()
        val captor = argumentCaptor<File>()
        verify(mockFileDeleter).deleteDirectory(captor.capture())
        verifyTabPreviewDirectoryUse(captor.firstValue.absolutePath)
    }

    @Test
    fun whenDeletingOnlyPreviewForATabThenTabDirectoryRemoved() = runTest {
        val tabId = "ABC-123"
        testee.deletePreviewsForTab(tabId, currentPreviewImage = null)
        verify(mockFileDeleter).deleteDirectory(any())
    }

    @Test
    fun whenDeletingOldPreviewForATabButANewOneExistsThenOnlySinglePreviewImageDeleted() = runTest {
        val tabId = "ABC-123"
        val newTabPreviewFilename = "new.jpg"
        val captor = argumentCaptor<List<String>>()
        testee.deletePreviewsForTab(tabId, newTabPreviewFilename)
        verify(mockFileDeleter).deleteContents(any(), captor.capture())
        verifyExistingPreviewExcludedFromDeletion(captor.firstValue, newTabPreviewFilename)
    }

    private fun verifyExistingPreviewExcludedFromDeletion(
        exclusionList: List<String>,
        newTabPreviewFilename: String,
    ) {
        assertEquals(1, exclusionList.size)
        assertTrue(exclusionList.contains(newTabPreviewFilename))
    }

    private fun verifyTabIdUsedAsDirectory(
        tabId: String,
        path: File,
    ) {
        assertTrue(path.parent.endsWith(tabId))
    }

    private fun verifyCacheDirectoryUsed(path: String) {
        assertTrue(path.startsWith(context.cacheDir.absolutePath))
    }

    private fun verifyTabPreviewDirectoryUse(path: String) {
        assertTrue(path.contains("/${FileBasedWebViewPreviewPersister.TAB_PREVIEW_DIRECTORY}"))
    }
}
