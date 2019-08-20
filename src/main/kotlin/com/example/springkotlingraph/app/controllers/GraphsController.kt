package com.example.springkotlingraph.app.controllers

import com.example.springkotlingraph.app.entities.render
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
import com.example.springkotlingraph.app.services.graph.GraphSaveService
import com.example.springkotlingraph.app.views.graph.GraphView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["/graphs"])
class GraphsController(
        val graphRepository: GraphRepository,
        val graphSaveService: GraphSaveService
) {
    @GetMapping
    fun index(): List<GraphView> = graphRepository.findAll().map { it.render() }

    @PostMapping("/{graphId}/save_all")
    fun save(@PathVariable graphId: UUID, @RequestBody request: GraphSaveParams): Boolean {
        val graph = graphSaveService.update(graphId, request)
        return true
    }
}
