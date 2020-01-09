package com.example.springkotlingraph.app.controllers

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.nodes.Node
import com.example.springkotlingraph.app.entities.nodes.render
import com.example.springkotlingraph.app.entities.render
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
import com.example.springkotlingraph.app.services.graph.GraphSaveService
import com.example.springkotlingraph.app.views.graph.GraphDataView
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

    @GetMapping("/{graphId}/data")
    fun data(@PathVariable graphId: UUID): GraphDataView = graphRepository
            .findById(graphId)
            .orElseThrow { Exception("Wat") }
            .let { graph ->
                GraphDataView(
                        graph = graph.render(),
                        nodes = graph.nodes.map { it.render() },
                        edges = graph.uniqueEdges().map { it.render() }
                )
            }

    @GetMapping("/save")
    fun dummySave() {
        val graph = Graph(name = "Test graph dummy save ")
        Node(name = "test node dummy", x = 0F, y = 0F, graph = graph)
        graphRepository.save(graph)
    }

    @PostMapping("/{graphId}/save_all")
    fun save(@PathVariable graphId: UUID, @RequestBody request: GraphSaveParams): Boolean {
        val graph = graphSaveService.update(graphId, request)
        // TODO if errors return them here
        return true
    }
}
