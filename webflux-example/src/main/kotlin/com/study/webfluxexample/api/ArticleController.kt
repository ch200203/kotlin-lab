package com.study.webfluxexample.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class ArticleController(

) {

    @GetMapping("/articles/{id}")
    suspend fun getArticle(@PathVariable("id") id: Long): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(ArticleResponse(1L, "집에 가고싶은 사람", "졸라집에 가고싶다.", LocalDateTime.now().toString(), "인철환"))
    }
}

data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val author: String,
)