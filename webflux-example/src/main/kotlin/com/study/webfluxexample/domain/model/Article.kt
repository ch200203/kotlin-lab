package com.study.webfluxexample.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class Article(
    @Id
    val id: Long? = null,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)
