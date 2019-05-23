package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.EdgeId
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
        return if (params.graph.id != null) {
            update(params)
        } else {
            create(params)
        }
    }

    private fun create(params: GraphSaveParams): Graph {
        val graph = upsert(params.graph)
        params.nodes.forEach { Node(name = it.name, graph = graph) }
        return graphRepository.save(graph)
    }

    private fun update(params: GraphSaveParams): Graph {
        upsertNodes(params)
        deleteNodes(params)
        saveEdges(params)
        deleteEdges(params)
        return upsert(params.graph)
    }

    private fun upsert(graphParams: GraphParams): Graph {
        return if (graphParams.id == null) {
            Graph(name = graphParams.name)
        } else {
            val graph = getGraph(graphParams.id)
            graph.name = graphParams.name
            graph
        }
    }

    private fun upsertNodes(params: GraphSaveParams) {
        val graph = getGraph(params.graph.id)
        params.nodes.forEach {
            if (it.id == null) {
                Node(name = it.name, graph = graph)
            } else {
                val persistedNode = graph.nodes.find { node -> node.id == it.id }
                persistedNode!!.name = it.name
            }
        }
    }

    private fun deleteNodes(params: GraphSaveParams) {
        val graph = getGraph(params.graph.id)
        val persistedNodeIds: List<Long> = graph.nodes.map { it.id }.filterNotNull()
        val newNodeIds = params.nodes.map { it.id }.filterNotNull()
        val toBeDeleted = persistedNodeIds.minus(newNodeIds)
        graph.deleteNodes(toBeDeleted)
    }

    private fun saveEdges(params: GraphSaveParams) {
        val graph = getGraph(params.graph.id)
        params.edges.forEach {
            val fromNode = graph.nodeById(it.fromNodeId)
            val toNode = graph.nodeById(it.toNodeId)
            if (fromNode is Node && toNode is Node) {
                Edge(
                        id = EdgeId(fromNodeId = fromNode.id!!.toLong(), toNodeId = toNode.id!!.toLong()),
                        fromNode = fromNode,
                        toNode = toNode
                )
            } else {
                throw EntityNotFound("Could not find fromNode or toNode")
            }
        }
    }

    private fun deleteEdges(params: GraphSaveParams) {
        val graph = getGraph(params.graph.id)
        graph.uniqueEdges().forEach {
            val edgeInParams = params.edges.find { edgeParams ->
                edgeParams.fromNodeId == it.fromNode.id && edgeParams.toNodeId == it.toNode.id
            }
            if (edgeInParams == null) {
                graph.removeEdge(it)
            }
        }
    }

    private fun getGraph(id: Long?): Graph {
        if (id == null) throw Exception("Graph can not be updated without primary key")
        return graphRepository.findById(id).orElseThrow { EntityNotFound(id) }
    }
}

data class GraphSaveParams(val graph: GraphParams, val nodes: MutableSet<NodeParams> = mutableSetOf(), val edges: MutableSet<EdgeParams> = mutableSetOf())
data class GraphParams(val id: Long? = null, val name: String)
data class NodeParams(val id: Long? = null, val name: String)
data class EdgeParams(val fromNodeId: Long, val toNodeId: Long)
