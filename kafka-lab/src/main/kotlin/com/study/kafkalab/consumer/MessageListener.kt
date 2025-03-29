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

    @KafkaListener(
        topics = ["\${kafka.topic-with-priority}"],
        containerFactory = "highPriorityKafkaListenerContainerFactory"
    )
    fun listenPriorityTopic(record: Any?) {
        logger.info("Received high priority message: {}", record)
    }

    @KafkaListener(
        topics = ["\${kafka.topic-with-priority}"],
        containerFactory = "normalPriorityKafkaListenerContainerFactory"
    )
    fun listenNonPriorityTopic(record: Any?) {
        logger.info("Received normal priority message: {}", record)
    }

    @KafkaListener(
        topics = ["\${kafka.topic-default-error}"],
        containerFactory = "errorCommonHandlingKafkaListenerContainerFactory"
    )
    fun listenForDefaultErrorHandle(record: Any?) {
        logger.info("Receive Message for Default Error Handler, It will occur error: $record")
        throw RuntimeException("Consumer Error and Exception Occurs.") // 이용하여 메시지가 수신하면 그대로 RuntimeException을 발생시켰다. 즉, 레코드를 수신받으면 바로 예외를 발생 시켰다.
    }


    @KafkaListener(
        topics = ["\${kafka.topic-error-handle}"],
        containerFactory = "errorHandlingKafkaListenerContainerFactory"
    )
    fun listenForErrorHandle(record: Any?) {
        logger.info("Receive Message for Error Handler, It will occur error: $record")
        throw RuntimeException("Consumer Error and Exception Occurs.")
    }

}
