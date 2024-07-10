package com.techyourchance.coroutines.solutions.exercise7

import com.techyourchance.coroutines.common.TestUtils
import com.techyourchance.coroutines.common.TestUtils.printCoroutineScopeInfo
import com.techyourchance.coroutines.common.TestUtils.printJobsHierarchy
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.lang.Exception
import kotlin.coroutines.EmptyCoroutineContext

class Exercise7SolutionTest {

    /*
    Write nested withContext blocks, explore the resulting Job's hierarchy, test cancellation
    of the outer scope
     */
    @Test
    fun nestedWithContext() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)

            scope.launch {
                try {
                    delay(1000)
                    withContext(CoroutineName("with Context")) {
                        try {
                            delay(1000)
                            withContext(CoroutineName("nested with context")) {
                                try {
                                    delay(1000)
                                    println("nested completed")
                                    printJobsHierarchy(scopeJob)
                                } catch (e: CancellationException) {
                                    println("nested cancelled")
                                }
                            }
                            println("withcontext completed")
                        } catch (e: CancellationException) {
                            println("withcontext cancelled")
                        }

                    }
                    println("coroutine completed")
                } catch (e: CancellationException) {
                    println("coroutine cancelled")
                }

            }

            launch {
                delay(2500)
                scope.cancel()
            }
            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine inside another coroutine, explore the resulting Job's hierarchy, test cancellation
    of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedLaunchBuilders() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)

            scope.launch {
                try {
                    delay(1000)
                    withContext(CoroutineName("with Context")) {
                        try {
                            delay(1000)
                           launch {
                                try {
                                    delay(1000)
                                    printJobsHierarchy(scopeJob)
                                    println("nested completed")

                                } catch (e: CancellationException) {
                                    println("nested cancelled")
                                }
                            }
                            println("withcontext completed")
                        } catch (e: CancellationException) {
                            println("withcontext cancelled")
                        }

                    }
                    println("coroutine completed")
                } catch (e: CancellationException) {
                    println("coroutine cancelled")
                }

            }

            launch {
                delay(3500)
                scope.cancel()
            }

            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine on "outer scope" inside another coroutine, explore the resulting Job's hierarchy,
    test cancellation of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedCoroutineInOuterScope() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)

            scope.launch {
                try {
                    delay(1000)
                    withContext(CoroutineName("with Context")) {
                        try {
                            delay(1000)
                            scope.launch {
                                try {
                                    delay(1000)
                                    printJobsHierarchy(scopeJob)
                                    println("nested completed")

                                } catch (e: CancellationException) {
                                    println("nested cancelled")
                                }
                            }
                            println("withcontext completed")
                        } catch (e: CancellationException) {
                            println("withcontext cancelled")
                        }

                    }
                    println("coroutine completed")
                } catch (e: CancellationException) {
                    println("coroutine cancelled")
                }

            }

            launch {
                delay(2500)
                scope.cancel()
            }

            scopeJob.join()
            println("test done")
        }
    }


}