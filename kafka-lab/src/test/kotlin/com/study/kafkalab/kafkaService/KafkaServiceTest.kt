package com.study.kafkalab.kafkaService

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringBootTest
@ExtendWith(SpringExtension::class)
@SpringJUnitConfig
@EmbeddedKafka(partitions = 1, brokerProperties = ["auto-offset-seconds=0"])
class KafkaServiceTest {

    @Test
    fun test(broker: EmbeddedKafkaBroker) {
        val brokersAsString = broker.brokersAsString
    }

}