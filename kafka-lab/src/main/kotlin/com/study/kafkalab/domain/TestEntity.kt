package com.study.kafkalab.domain

import java.time.LocalDateTime

data class TestEntity(
    var title: String,
    var contents: String,
    var time: LocalDateTime? = null,
)