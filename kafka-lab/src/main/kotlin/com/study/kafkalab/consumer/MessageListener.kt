package com.study.kafkalab.consumer

import com.study.kafkalab.config.KafkaTopicConfig
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class MessageListener {
    companion object {
        private val logger = LoggerFactory.getLogger(MessageListener::class.java)
    }

    @KafkaListener(
        topics = [KafkaTopicConfig.DEFAULT_TOPIC],
        containerFactory = "defaultKafkaListenerContainerFactory",
        autoStartup = "true"
    )
    fun listenDefaultTopic(record: Any?) {
        logger.info("Receive Message from ${KafkaTopicConfig.DEFAULT_TOPIC}, values :  $record")
    }


    @KafkaListener(
        topics = ["\${kafka.topic-with-key}"],
        containerFactory = "defaultKafkaListenerContainerFactory",
        autoStartup = "true"
    )
    fun listenTopicWithKey(record: Any?) {
        logger.info("Receive Message from $record")
    }

}
