package com.study.coboardexample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

fun main() {
    runBlocking {
        val results = mutableListOf<Int>()

        val jobs = List(100) {
            launch {
                withContext(Dispatchers.IO) {
                    sleep(100L)
                    synchronized(results) {
                        results.add(it)
                    }
                }
                println("Adding $it on Thread: ${Thread.currentThread()}")
            }
        }
        jobs.forEach { it.join() }
    }
}