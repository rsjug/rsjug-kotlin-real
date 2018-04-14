package org.rsjug.todolist.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@RequestMapping("/")
class IndexController() {

    @GetMapping("")
    fun index() = "hello world"

}