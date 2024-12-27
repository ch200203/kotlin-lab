package com.study.kafkalab.controller

import com.study.kafkalab.config.KafkaTopicConfig
import com.study.kafkalab.domain.TestEntity
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture


@RestController
@RequestMapping("/api")
class ProducerController(
    // kafka producer를 위한 KafkaTemplate를 지정한다.s
    val kafkaProducerTemplate: KafkaTemplate<String, Any>
) {

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
                    "Message sent successfully with offset: {}, partition: {}",
                    metadata.offset(),
                    metadata.partition()
                )
            }
        }

        return ResponseEntity.ok(testEntity)
    }

}