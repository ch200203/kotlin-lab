package com.study.exposedspringexample.domain

import org.jetbrains.exposed.dao.id.LongIdTable

object UserEntity: LongIdTable() {
    val name = varchar("name", 50)
    val age = integer("age")
}