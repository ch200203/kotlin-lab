package com.study.webfluxexample

import com.study.webfluxexample.domain.model.Article
import com.study.webfluxexample.infrastructure.ArticleRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.test.StepVerifier
import java.time.Instant

@DataMongoTest
class ArticleRepositoryTest(
    @Autowired
    private val template: ReactiveMongoTemplate,
    @Autowired
    private val repository: ArticleRepository
) {

    @BeforeEach
    fun beforeEach() {
        template.dropCollection(Article::class.java).subscribe()
    }

    private fun generateDocument(): Article {
        return template.insert(
            Article(
                id = null,
                title = "title",
                content = "content",
                author = "abc1234",
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        ).block()!!
    }

    @Test
    fun `save will create data correctly`() {
        // Arrange
        val author = "abc1234"
        val title = "title"
        val content = "content"
        val document: Article = Article(
            id = null,
            title = title,
            content = content,
            author = author,
        )

        // Act
        repository.save(document)
            .`as` { StepVerifier.create(it) }
            .assertNext {
                assertThat(it.id).isNotNull()
                assertThat(it.name).isEqualTo(name)
                assertThat(it.age).isEqualTo(age)
            }
            .verifyComplete()
    }
}