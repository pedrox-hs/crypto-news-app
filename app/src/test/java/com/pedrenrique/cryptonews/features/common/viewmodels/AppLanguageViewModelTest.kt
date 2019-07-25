package com.pedrenrique.cryptonews.features.common.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pedrenrique.cryptonews.core.platform.Language
import com.pedrenrique.cryptonews.core.platform.LocaleManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AppLanguageViewModelTest {
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var localeManager: LocaleManager
    @Mock
    private lateinit var observer: Observer<AppLanguageViewModel.State>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should initialize language en`() {
        `when`(localeManager.getLanguage())
            .thenReturn(Language.ENGLISH)

        val viewModel = AppLanguageViewModel(localeManager)
        assertEquals(viewModel.language, Language.ENGLISH)
    }

    @Test
    fun `should initialize language pt`() {
        `when`(localeManager.getLanguage())
            .thenReturn(Language.PORTUGUESE)

        val viewModel = AppLanguageViewModel(localeManager)
        assertEquals(viewModel.language, Language.PORTUGUESE)
    }

    @Test
    fun `should change language to pt`() {
        `when`(localeManager.getLanguage())
            .thenReturn(Language.ENGLISH)

        val viewModel = AppLanguageViewModel(localeManager)
        viewModel.state.observeForever(observer)

        verify(observer).onChanged(AppLanguageViewModel.State.Done)
        verifyNoMoreInteractions(observer)

        viewModel.setNewLanguage(Language.PORTUGUESE.ordinal)

        verify(observer).onChanged(AppLanguageViewModel.State.RefreshNeeded)
        verifyNoMoreInteractions(observer)

        viewModel.setRefreshDone()

        verify(observer, times(2)).onChanged(AppLanguageViewModel.State.Done)
        verifyNoMoreInteractions(observer)

        assertEquals(viewModel.language, Language.PORTUGUESE)
    }

    @Test
    fun `should change language to en`() {
        `when`(localeManager.getLanguage())
            .thenReturn(Language.PORTUGUESE)

        val viewModel = AppLanguageViewModel(localeManager)
        viewModel.state.observeForever(observer)

        verify(observer).onChanged(AppLanguageViewModel.State.Done)
        verifyNoMoreInteractions(observer)

        viewModel.setNewLanguage(Language.ENGLISH.ordinal)

        verify(observer).onChanged(AppLanguageViewModel.State.RefreshNeeded)
        verifyNoMoreInteractions(observer)

        viewModel.setRefreshDone()

        verify(observer, times(2)).onChanged(AppLanguageViewModel.State.Done)
        verifyNoMoreInteractions(observer)

        assertEquals(viewModel.language, Language.ENGLISH)
    }
}