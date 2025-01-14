package com.study.kafkalab

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class TestPropertyTest {

    lateinit var testconfig: TestConfig

    @Test
    fun 프로퍼티읽어오기() {
        println(testconfig.toString())
        assertEquals(testconfig.value, "hello-world")
    }

}

@EnableConfigurationProperties(TestProperty::class)
@TestConfiguration(proxyBeanMethods = false)
class TestConfig(testProperty: TestProperty) {
    var value: String? = testProperty.value
}