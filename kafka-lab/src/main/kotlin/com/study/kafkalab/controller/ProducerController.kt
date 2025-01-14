package com.study.kafkalab.controller

import com.study.kafkalab.config.KafkaTopicConfig
import com.study.kafkalab.domain.TestEntity
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.CompletableFuture


@RestController
@RequestMapping("/api")
class ProducerController(
    // kafka producer를 위한 KafkaTemplate를 지정한다.
    val kafkaProducerTemplate: KafkaTemplate<String, Any>
) {

    @Value("\${kafka.topic-with-key}")
    private lateinit var TOPIC_WITH_KEY: String

    companion object {
        private val logger = LoggerFactory.getLogger(ProducerController::class.java)
    }


    @PostMapping("produce")
    fun produceMessage(@RequestBody testEntity: TestEntity): ResponseEntity<TestEntity> {
        testEntity.time = LocalDateTime.now()

        // KafkaTemplate.send()는 CompletableFuture로 비동기 결과를 반환
        val future: CompletableFuture<SendResult<String, Any>> =
            kafkaProducerTemplate.send(KafkaTopicConfig.DEFAULT_TOPIC, testEntity)

        // CompletableFuture로 결과 처리
        future.whenComplete { result, ex ->
            if (ex != null) {
                logger.error("Failed to send message to broker: {}", ex.message, ex)
            } else {
                val metadata: RecordMetadata = result.recordMetadata
                logger.info(
                    "Message sent successfully with offset: ${metadata.offset()}, partition: ${metadata.partition()}",
                )
            }
        }

        return ResponseEntity.ok(testEntity)
    }

    // key 를 통해서 분배.
    @PostMapping("/produce-with-key/{key}")
    fun produceMessageWithKey(
        @PathVariable("key") key: String,
        @RequestBody testEntity: TestEntity
    ): ResponseEntity<TestEntity> {

        testEntity.time = LocalDateTime.now()

        // key 를 포함하여 전달한다.
        val future: CompletableFuture<SendResult<String, Any>> =
            kafkaProducerTemplate.send(TOPIC_WITH_KEY, key, testEntity)

        // 결과 처리
        future.whenComplete { result, ex ->
            if (ex != null) {
                logger.error("Failed to send message to broker: {}", ex.message, ex)
            } else {
                val metadata: RecordMetadata = result.recordMetadata
                logger.info("Sent Message with key: ${key}, offset: ${metadata.offset()}, partition: ${metadata.partition()}")
            }
        }

        return ResponseEntity.ok(testEntity)
    }
}