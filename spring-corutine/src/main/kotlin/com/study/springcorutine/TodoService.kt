package com.study.springcorutine

import com.study.springcorutine.client.TodoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerResponse.async

@Service
class TodoService(
    private val todoClient: TodoClient
) {

    private val logger = KotlinLogging.logger {}

    suspend fun todo(id: Long) {
        val todo = async { todoClient.getTodo(id) }

        logger.info { todo.toString() }

        val post = async { todoClient.getPost(id) }

        logger.info { todo.toString() }
    }

    suspend fun todoCoroutine(id: Long) {
        runBlocking {
            // FeignClient의 동기 호출을 비동기적으로 처리
            val todo = todoClient.getTodo(id)
            logger.info { "Todo: $todo" }

            val post = todoClient.getPost(id)
            logger.info { "Post: $post" }

            // 결과 반환
            listOf(todo, post)
        }

    }

}
