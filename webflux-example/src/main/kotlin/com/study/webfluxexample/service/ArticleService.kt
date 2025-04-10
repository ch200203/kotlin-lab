package com.study.webfluxexample.service

import com.study.webfluxexample.domain.model.Article
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ArticleService {

    suspend fun getAll(): Flux<Article> {
        return Flux.empty()
    }
}