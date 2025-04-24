package com.design.ch5

import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Test
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch

class MultiThreadSingletonInstanceTest {
    /**
     * 멀티 스레드를 사용하는 경우 싱글톤이 깨질 수 있는 케이스 테스트
     */
    @Test
    fun singletonMultiThreadTest() {
        val threadCount = 2
        val instances = CopyOnWriteArrayList<DoubleCheckClass>()
        val latch = CountDownLatch(threadCount)

        repeat(threadCount) {
            Thread {
                val instance = MyTestClass.getInstance()

                println("Thread-$it instance: ${System.identityHashCode(instance)}")

                instances.add(instance)
                latch.countDown()
            }.start()
        }

        latch.await()

        val instance0 = instances[0]
        val instance1 = instances[1]

        assertNotSame(instance0, instance1)
    }
}

class MyTestClass private constructor() {
    companion object {
        private var instance: DoubleCheckClass? = null

        fun getInstance(): DoubleCheckClass {
            if (instance == null) {
                instance = DoubleCheckClass()
            }

            return instance!!
        }
    }
}


class MyTestClassV2 private constructor() {
    companion object {
        private var instance: MyTestClassV2? = null

        @Synchronized
        fun getInstance(): MyTestClassV2 {
            if (instance == null) {
                instance = MyTestClassV2()
            }

            return instance!!
        }
    }
}

class DoubleCheckClass private constructor() {
    companion object {
        @Volatile
        private var instance: DoubleCheckClass? = null

        fun getInstance(): DoubleCheckClass {
            // 첫 번째 체크 (락 없이)
            if (instance == null) {
                synchronized(this) {
                    // 두 번째 체크 (락 안에서)
                    if (instance == null) {
                        instance = DoubleCheckClass()
                    }
                }
            }
            return instance!!
        }
    }
}
