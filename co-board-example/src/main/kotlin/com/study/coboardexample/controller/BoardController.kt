package com.study.coboardexample.controller

import com.study.coboardexample.service.BoardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/board")
class BoardController(
    private val boardService: BoardService,
) {

    @PostMapping("/create")
    fun createBoard(@RequestBody request: BoardRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(boardService.saveBoard(request))
    }

}

data class BoardRequest(
    val title: String,
    val content: String,
)
