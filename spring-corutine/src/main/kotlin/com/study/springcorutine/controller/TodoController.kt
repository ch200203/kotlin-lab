package com.study.springcorutine.controller

import com.study.springcorutine.TodoService
import com.study.springcorutine.client.TodoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoController(
    val todoService: TodoService,
    val todoClient: TodoClient
) {

    @GetMapping("/todo/{id}")
    fun getTodo(@PathVariable id: Long): ResponseEntity<Any> {
        // todoService.todoCoroutine(id)
        return ResponseEntity.ok("")
    }

    @GetMapping("/todo/{id}/co")
    suspend fun getTodoCoroutine(@PathVariable id: Long): ResponseEntity<Any> {
        return ResponseEntity.ok(todoService.todoCoroutine(id))
    }
}
