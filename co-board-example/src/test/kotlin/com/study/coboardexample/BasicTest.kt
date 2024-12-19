package com.study.coboardexample

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
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

}
