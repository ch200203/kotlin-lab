package com.study.kafkalab.config

import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaTopicConfig(
    // kafka admin bean 주입
    val kafkaAdmin: KafkaAdmin,
) {


    @Value("\${kafka.topic-with-key}")
    private lateinit var TOPIC_WITH_KEY: String

    companion object {
        const val DEFAULT_TOPIC: String = "DEFAULT_TOPIC"
    }

    private fun defaultTopic(): NewTopic =
        TopicBuilder.name(DEFAULT_TOPIC)
            .partitions(2) // 카프카 플러스터 3개의 브로커로 구성함 -> 파티션 2개, 복제계수 2로 지정
            .replicas(2)
            .build()

    private fun topicWithKey(): NewTopic =
        TopicBuilder.name(TOPIC_WITH_KEY)
            .partitions(2)
            .replicas(2)
            .build()

    /**
     * @PostConstruct : 빈이 생성되고 나서 마지막으로 수행
     * createOrModifyTopics : 토픽을 생성 하거나, 변경하도록 한다.
     */
    @PostConstruct
    fun init() {
        kafkaAdmin.createOrModifyTopics(defaultTopic())
        kafkaAdmin.createOrModifyTopics(topicWithKey())
    }
}