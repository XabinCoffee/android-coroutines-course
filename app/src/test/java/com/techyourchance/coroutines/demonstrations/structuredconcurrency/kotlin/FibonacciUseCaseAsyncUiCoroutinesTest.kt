package com.techyourchance.coroutines.demonstrations.structuredconcurrency.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

@ExperimentalCoroutinesApi
class FibonacciUseCaseAsyncUiCoroutinesTest {

    private lateinit var callback: FibonacciUseCaseAsyncUiCoroutines.Callback
    private lateinit var SUT: FibonacciUseCaseAsyncUiCoroutines

    var lastResult: BigInteger? = null
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        callback = object : FibonacciUseCaseAsyncUiCoroutines.Callback {
            override fun onFibonacciComputed(result: BigInteger?) {
                lastResult = result
            }
        }
        SUT = FibonacciUseCaseAsyncUiCoroutines(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun computeFibonacci_0_returns0() {
        // Dispatcher is sent is an optional param
        runTest(testCoroutineDispatcher) {
            // Arrange
            // Act
            SUT.computeFibonacci(0, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("0")))
        }
    }

    @Test
    fun computeFibonacci_1_returns1() {
        runTest {
            // Arrange
            // Act
            SUT.computeFibonacci(1, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("1")))
        }
    }

    @Test
    fun computeFibonacci_10_returnsCorrectAnswer() {
        runTest {
            // Arrange
            // Act
            SUT.computeFibonacci(10, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("55")))
        }
    }

    @Test
    fun computeFibonacci_30_returnsCorrectAnswer() {
        runTest {
            // Arrange
            // Act
            SUT.computeFibonacci(30, callback)
            // Assert
            assertThat(lastResult, `is`(BigInteger("832040")))
        }
    }

}