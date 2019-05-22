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
        val graph = if (params.graph.id != null) {
            this.update(params)
        } else {
            this.create(params)
        }
        return graphRepository.save(graph)
    }

    private fun create(params: GraphSaveParams): Graph {
        val graph = Graph(name = params.graph.name)
        params.nodes.forEach { Node(name = it.name, graph = graph) }
        return graph
    }

    private fun update(params: GraphSaveParams): Graph {
        val graph = this.getGraph(params.graph.id)
        this.updateNodes(params)
        this.createNodes(params)
        this.deleteNodes(params)
        graph.name = params.graph.name
        return graph
    }

    private fun updateNodes(params: GraphSaveParams) {
        val graph = this.getGraph(params.graph.id)
        params.nodes.filter { it.id != null }.forEach {
            val persistedNode = graph.nodes.find { node -> node.id == it.id }
            persistedNode!!.name = it.name
        }
    }

    private fun createNodes(params: GraphSaveParams) {
        val graph = this.getGraph(params.graph.id)
        params.nodes.filter { it.id == null }.forEach {
            Node(name = it.name, graph = graph)
        }
    }

    private fun deleteNodes(params: GraphSaveParams) {
        val graph = this.getGraph(params.graph.id)
        val persistedNodeIds: List<Long> = graph.nodes.map { it.id }.filterNotNull()
        val newNodeIds = params.nodes.map { it.id }.filterNotNull()
        val toBeDeleted = persistedNodeIds.minus(newNodeIds)
        graph.deleteNodes(toBeDeleted)
    }

    private fun getGraph(id: Long?): Graph {
        if (id == null) throw Exception("Graph can not be updated without primary key")
        return graphRepository.findById(id).orElseThrow { EntityNotFound(id) }
    }
}

data class GraphSaveParams(val graph: GraphParams, val nodes: MutableSet<NodeParams> = mutableSetOf())
data class GraphParams(val id: Long? = null, val name: String)
data class NodeParams(val id: Long? = null, val name: String)
