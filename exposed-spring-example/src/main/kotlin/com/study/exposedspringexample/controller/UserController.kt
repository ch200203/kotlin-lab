package com.study.exposedspringexample.controller

import com.study.exposedspringexample.domain.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/api/v1/{userId}")
    fun getUserOne(@PathVariable userId: Long): User {

    }



}