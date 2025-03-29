package com.study.webfluxexample.infrastructure

import com.study.webfluxexample.domain.model.Article
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ArticleRepository: ReactiveMongoRepository<Article, String> {
}
