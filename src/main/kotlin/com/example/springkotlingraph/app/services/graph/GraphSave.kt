package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.Node
import com.example.springkotlingraph.app.exceptions.EntityNotFound
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GraphSave(private val graphRepository: GraphRepository) {
    fun save(params: GraphSaveParams): Graph {
        val graph = this.toGraph(params)
        return graphRepository.save(graph)
    }

    private fun toGraph(params: GraphSaveParams): Graph {
        return if (params.graph.id != null) {
            val graph = graphRepository.findById(params.graph.id).orElseThrow { EntityNotFound(params.graph.id) }
            val persistedNodeIds: List<Long> = graph.nodes.map { it.id }.filterNotNull()
            val newNodeIds = params.nodes.map { it.id }.filterNotNull()
            val toBeDeleted = persistedNodeIds.minus(newNodeIds)
            params.nodes.forEach {
                val persistedNode = graph.nodes.find { node -> node.id == it.id }
                persistedNode!!.name = it.name
            }
            graph.deleteNodes(toBeDeleted)
            graph.name = params.graph.name
            graph
        } else {
            val graph = Graph(name = params.graph.name)
            params.nodes.forEach { Node(name = it.name, graph = graph) }
            graph
        }
    }
}

data class GraphSaveParams(val graph: GraphParams, val nodes: MutableSet<NodeParams> = mutableSetOf())
data class GraphParams(val id: Long? = null, val name: String)
data class NodeParams(val id: Long? = null, val name: String)
