

package com.duckduckgo.app.widget.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command.Close
import com.duckduckgo.app.widget.ui.AddWidgetInstructionsViewModel.Command.ShowHome
import com.duckduckgo.common.test.InstantSchedulersRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

class AddWidgetInstructionsViewModelTest {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    @Suppress("unused")
    val schedulers = InstantSchedulersRule()

    private lateinit var testee: AddWidgetInstructionsViewModel

    @Mock
    private lateinit var mockCommandObserver: Observer<Command>

    private val commandCaptor = argumentCaptor<Command>()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        testee = AddWidgetInstructionsViewModel()
        testee.command.observeForever(mockCommandObserver)
    }

    @After
    fun after() {
        testee.command.removeObserver(mockCommandObserver)
    }

    @Test
    fun whenShowHomePressedThenShowHomeCommandSent() {
        testee.onShowHomePressed()
        verify(mockCommandObserver).onChanged(commandCaptor.capture())
        assertEquals(ShowHome, commandCaptor.lastValue)
    }

    @Test
    fun whenClosePressedThenCloseCommandSent() {
        testee.onClosePressed()
        verify(mockCommandObserver).onChanged(commandCaptor.capture())
        assertEquals(Close, commandCaptor.lastValue)
    }
}
