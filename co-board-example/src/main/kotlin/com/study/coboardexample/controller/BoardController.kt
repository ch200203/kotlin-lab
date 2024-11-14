package com.study.coboardexample.controller

import com.study.coboardexample.service.BoardService
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/info")
    fun getBoardInfo(@ParameterObject request: BoardRequest): ResponseEntity<Any> {
        println(request.toString())
        val response = BoardResponse().apply {
            title = request.title
            content = request.content
        }
        return ResponseEntity.ok("BoardResponse: $response")
    }
}

class BoardRequest {

    @Schema(description = "게시물 제목")
    var title: String? = null

    @Schema(description = "게시물 내용")
    var content: String? = null

    override fun toString(): String {
        return "BoardRequest(content=$content, title=$title)"
    }

}

class BoardResponse {

    @Schema(description = "게시물 제목")
    var title: String? = null

    @Schema(description = "게시물 내용")
    var content: String? = null

    override fun toString(): String {
        return "BoardResponse(title=$title, content=$content)"
    }
}
