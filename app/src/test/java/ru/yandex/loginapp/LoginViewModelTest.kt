package ru.yandex.loginapp

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        viewModel = LoginViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        val expected = LoginScreenState.EmptyFieldsError
        viewModel.login("", "")
        val actual = viewModel.state.value
        assertEquals(expected, actual)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        val expected = LoginScreenState.EmailValidationError
        viewModel.login("email", "password")
        val actual = viewModel.state.value
        assertEquals(expected, actual)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        val expected = LoginScreenState.Loading
        viewModel.login("test@email.com", "password")
        runCurrent()
        val actual = viewModel.state.value
        assertEquals(expected, actual)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        val expectedLoading = LoginScreenState.Loading
        viewModel.login("test@email.com", "password")
        runCurrent()
        assertEquals(expectedLoading, viewModel.state.value)

        advanceTimeBy(3001)
        val expectedSuccess = LoginScreenState.Success
        assertEquals(expectedSuccess, viewModel.state.value)
    }

}