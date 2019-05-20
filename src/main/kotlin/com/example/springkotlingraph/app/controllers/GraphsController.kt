package com.example.springkotlingraph.app.controllers

import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController(val graphRepository: GraphRepository) {
    @GetMapping
    fun index() = graphRepository.findAll()
}
