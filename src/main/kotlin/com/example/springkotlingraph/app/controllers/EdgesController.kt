package com.example.springkotlingraph.app.controllers

import com.example.springkotlingraph.app.entities.render
import com.example.springkotlingraph.app.exceptions.EntityNotFound
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.views.edge.EdgeView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["/graphs/{graphId}"])
class EdgesController(val graphRepository: GraphRepository) {
    @GetMapping("/edges")
    fun index(@PathVariable graphId: UUID): List<EdgeView> {
        val graph = graphRepository.findOneById(graphId) ?: throw EntityNotFound(graphId)
        return graph.uniqueEdges().map { it.render() }
    }
}
