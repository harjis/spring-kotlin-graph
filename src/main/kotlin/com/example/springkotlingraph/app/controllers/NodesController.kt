package com.example.springkotlingraph.app.controllers

import com.example.springkotlingraph.app.entities.nodes.render
import com.example.springkotlingraph.app.exceptions.EntityNotFound
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.views.node.NodeView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["/graphs/{graphId}"])
class NodesController(val graphRepository: GraphRepository) {
    @GetMapping("/nodes")
    fun index(@PathVariable graphId: UUID): List<NodeView> {
        val graph = graphRepository.findOneById(graphId) ?: throw EntityNotFound(graphId)
        return graph.nodes.map { it.render() }
    }
}
