package com.study.coboardexample

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.StructuredTaskScope
import kotlin.test.assertEquals

class BasicTest {

    // 코루틴 테스트 해보기

    @Test
    fun coroutineTest(): Unit = runBlocking {
        val results = mutableListOf<Int>()

        val jobs = List(100) {
            launch {
                // sleep(100L) // 코루틴 내에서 Thread.sleep 을 호출 하고 있어서... 별로 적잘하지 않음
                delay(100L) // 비동기 지연추가
                results.add(it)
                println("Adding $it on Thread: ${Thread.currentThread()}")
            }
        }

        jobs.forEach { it.join() }
        assertEquals(100, results.size)
    }

    @Test
    fun coroutineParallelTest() = runBlocking {
        val results = mutableListOf<Int>()

        val jobs = List(100_000) {
            launch {
                // WithContext 자체는 병렬 성을 제공하지 않음
                // 그러나, 디스패처를 변경하여 서로다른 스레드에서 병렬처리가 가능하도록 함, 따라서 스레드 블록이 되는 상황에서는
                // 아래의 코드가 적잘함
                withContext(Dispatchers.IO) {
                    // sleep(100L)
                    delay(100L)
                    synchronized(results) {
                        results.add(it)
                    }
                }
                println("Adding $it on Thread: ${Thread.currentThread()}")
            }
        }

        jobs.forEach { it.join() }
        assertEquals(100_000, results.size)
    }

    @Test
    fun coroutineParallelTestV2() {
        /**
         * CoroutineDispatcher 는 각 작업을 특정 스레드나 스레드 풀로 전달,
         * 작업 실행 요청이 들어오면, 해당 작업을 작업 대기열에 적재합니다. 이후 스레드 풀 내, 사용 가능한 스레드가 있다면 해당 스레드로 보내어 작업을 수행
         */
        val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher() // 4 개의 스레드 풀을 코루틴 디스패처로 변환

        // 명시적으로 스레드 풀을 설정
        runBlocking(dispatcher) {
            val results = mutableListOf<Int>()

            val jobs = List(100_000) {
                launch {
                    withContext(Dispatchers.IO) {
                        sleep(100L)
                        // synchronized 로 공유 자원(results) 보호
                        synchronized(results) {
                            results.add(it)
                        }
                    }
                    println("Adding $it on Thread: ${Thread.currentThread()}")
                }
            }

            jobs.forEach { it.join() }
            assertEquals(100_000, results.size)
        }
    }

    // 버추얼스레드 테스트 해보기
    @Test
    fun virtualThreadTest() {
        val results = mutableListOf<Int>()
        // 버추얼 스레드 사용
        val executor = Executors.newVirtualThreadPerTaskExecutor()

        val jobs = List(100) {
            executor.submit {
                // Thread sleep 이 일어날 경우 스레드 중단에 대응하려 다른 버추얼 스레드로 전환되어
                // WithContext 처럼 병렬 로직을 추가하지 않아도 좋다.
                // Virtual Thread는 sleep(100L)의 스레드 중단에 대응하여
                // 다른 Virtual Thread로 전환되므로 추가적인 병렬 처리 로직을 구현하지 않아도 자연스럽게 병렬 처리가 이루어집니다.
                sleep(100L)
                synchronized(results) {
                    results.add(it)
                }

                sleep(100L)
                println("Adding $it on Thread: ${Thread.currentThread()}")
            }
        }

        jobs.forEach { it.get() }
        assertEquals(100, results.size)
        executor.shutdown()
    }

    @Test
    fun structuredTest() {
        val results = mutableListOf<Int>()
        try {
            // 구조화된 동시성 개념을 Virtual Thread와 함께 구현한 것으로 동시 실행되는 작업들을 체계적으로 관리하고 그 생명주기를 명확하게 함
            StructuredTaskScope.ShutdownOnFailure().use { taskScope ->
                val jobs = List(100) {
                    taskScope.fork {
                        sleep(1000L)
                        if (it % 3 == 0) {
                            throw ClassNotFoundException()
                        }
                        synchronized(results) {
                            results.add(it)
                        }
                        println("Adding $it on Thread: ${Thread.currentThread()}")
                    }
                    taskScope.join()
                    taskScope.throwIfFailed()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("Results size: ${results.size}")
    }

    /**
     * newVirtualThreadPerTaskExecutor 를 사용하면 자동으로 관리해줘서 스레드 풀 개수를 지정하지 않아도 된다.
     */
    @Test
    fun manyVirtualThreadTest() {
        val threads = (1..10000).map {
            Thread.ofVirtual().start {
                try {
                    sleep(1000)
                    println("Thread $it finished, Thread: ${Thread.currentThread()}")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        threads.forEach { it.join() }
    }

    @Test
    fun improveCoroutineParallelTest() = runBlocking {
        // 스레드 별 로컬 리스트 저장
        val threadLocalResults = ConcurrentHashMap<Thread, MutableList<Int>>()

        val jobs = List(100_000) {
            launch(Dispatchers.IO) {
                delay(100L) //  비동기 지연
                threadLocalResults.computeIfAbsent(Thread.currentThread()) { mutableListOf() }.add(it)
                println("Thread $it finished, Thread: ${Thread.currentThread()}")

            }
        }

        jobs.forEach {
            it.join()
        }

        val results = threadLocalResults.values.flatten()
        assertEquals(100_000, results.size)
        println("Results size: ${results.size}")
    }

    /**
     * 코루틴과 버추얼 스레드 테스트환경 구축
     */

    // runBlocking과 같은 코루틴 스코프를 사용하면 동기식 코드처럼 코루틴을 실행하고 테스트 가능
    @Test
    fun coroutineSimpleTest() = runBlocking {
        // runBlocking을 사용하면 코루틴 함수가 끝날 때까지 대기하고 결과를 검증
        val result = getNumberSuspend()
        assertEquals(100, result)
    }

    private suspend fun getNumberSuspend(): Int {
        delay(1000)
        return 100
    }

    // 코루틴 테스트에서 중요한 점은 `Dispatcher` 를 제어 가능
    // 실제로 I/O를 사용하는 대신 Dispatchers.Unconfined나 TestCoroutineDispacher를 사용해 가상의 환경에서 테스트 가능
    @Test
    fun dispatcherTest() = runBlocking {
        withContext(Dispatchers.Unconfined) {
            (1..10).forEach {
                launch {
                    println("Unconfined$it: ${Thread.currentThread().name}")
                    val result = getNumberSuspend()
                    assertEquals(100, result)
                }
            }
        }
    }

}
