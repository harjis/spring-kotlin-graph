package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.exceptions.EntityNotFound
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GraphSave(private val graphRepository: GraphRepository) {
    fun save(params: GraphSaveParams): Graph {
        val graph = this.toGraph(params.graph)
        return graphRepository.save(graph)
    }

    private fun toGraph(graphParams: GraphParams): Graph {
        val graph: Graph = if (graphParams.id != null) {
            var _graph = graphRepository.findById(graphParams.id).orElseThrow { EntityNotFound(graphParams.id) }
            _graph.name = graphParams.name
            _graph
        } else {
            Graph(name = graphParams.name)
        }
        return graph
    }
}

data class GraphSaveParams(val graph: GraphParams)
data class GraphParams(val id: Long? = null, val name: String)
