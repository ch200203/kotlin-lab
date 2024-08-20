package com.study.springcorutine.client

import com.study.springcorutine.dto.Post
import com.study.springcorutine.dto.Todo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name = "studyClient", url = "https://jsonplaceholder.typicode.com/")
interface TodoClient {

    @RequestMapping(method = [RequestMethod.GET], value = ["/todos/{id}"])
    fun getTodo(@PathVariable id: Long): Todo

    @GetMapping("/posts/{id}")
    fun getPost(@PathVariable id: Long): Post

}
