package com.study.coboardexample.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Board(
    private var title: String,
    private var content: String,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }
}
