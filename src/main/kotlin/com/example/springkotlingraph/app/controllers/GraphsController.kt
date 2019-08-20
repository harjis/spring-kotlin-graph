package com.example.springkotlingraph.app.controllers

import com.example.springkotlingraph.app.entities.render
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.views.graph.GraphView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController(val graphRepository: GraphRepository) {
    @GetMapping
    fun index(): List<GraphView> = graphRepository.findAll().map { it.render() }
}
