package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.nodes.Node
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class GraphSaveService(private val graphRepository: GraphRepository) {
    fun save(params: GraphSaveParams): Graph {
        val (graphParams, nodeParams, edgeParams) = params
        val graph = Graph(name = graphParams.name, id = graphParams.id)
        val nodes = nodeParams.map { it.id to Node(graph, it.name, it.x, it.y, it.id) }.toMap()
        edgeParams.forEach { edge ->
            val fromNode = nodes[edge.fromNodeId] ?: throw IllegalArgumentException("")
            val toNode = nodes[edge.toNodeId] ?: throw IllegalArgumentException("")
            Edge(fromNode, toNode, edge.id)
        }
        return graphRepository.save(graph)
    }

    fun update(id: UUID, params: GraphSaveParams): Graph {
        if (!graphRepository.existsById(id)) throw IllegalArgumentException("")

        val (graphParam, nodeParams, edgeParams) = params
        val graph = graphRepository.findOneById(id)?.apply {
            name = graphParam.name
        } ?: throw IllegalStateException("")
        removeUnused(graph, nodeParams, edgeParams)

        val currentNodeIds = graph.nodes.map { it.id }
        val (oldNodes, newNodes) = nodeParams.partition { it.id in currentNodeIds }
        updateOldNodes(graph, oldNodes)
        addNewNodes(graph, newNodes)
        addNewEdges(graph, edgeParams)
        return graphRepository.save(graph)
    }

    private fun removeUnused(graph: Graph, nodeParams: List<NodeParams>, edgeParams: List<EdgeParams>) {
        val newNodeIds = nodeParams.map { it.id }
        val newEdgeIds = edgeParams.map { it.id }
        graph.removeNodeIf { it.id !in newNodeIds }
        graph.removeEdgeIf { it.id !in newEdgeIds }
    }

    private fun updateOldNodes(graph: Graph, nodeParams: Collection<NodeParams>) {
        val idToNodeParam = nodeParams.associateBy { it.id }
        graph.updateNodes { node ->
            val nodeParam = idToNodeParam[node.id] ?: return@updateNodes
            node.apply {
                name = nodeParam.name
                x = nodeParam.x
                y = nodeParam.y
            }
        }
    }

    private fun addNewNodes(graph: Graph, nodeParams: Collection<NodeParams>) {
        nodeParams.forEach { Node(graph, it.name, it.x, it.y, it.id) }
    }

    private fun addNewEdges(graph: Graph, edgeParams: Collection<EdgeParams>) {
        val currentEdges = graph.uniqueEdges().map { it.id }
        val newEdges = edgeParams.filter { it.id !in currentEdges }
        val nodeIdToNode = graph.nodes.associateBy { it.id }
        newEdges.forEach { edgeParam ->
            val fromNode = nodeIdToNode[edgeParam.fromNodeId] ?: throw IllegalStateException("")
            val toNode = nodeIdToNode[edgeParam.toNodeId] ?: throw IllegalStateException("")
            Edge(fromNode, toNode, edgeParam.id)
        }
    }

}

data class GraphSaveParams(
        val graph: GraphParams,
        val nodes: List<NodeParams> = emptyList(),
        val edges: List<EdgeParams> = emptyList()
)

data class GraphParams(val name: String, val id: UUID = UUID.randomUUID())
data class NodeParams(val name: String, val x: Float = 0F, val y: Float = 0F, val id: UUID = UUID.randomUUID())
data class EdgeParams(val fromNodeId: UUID, val toNodeId: UUID, val id: UUID = UUID.randomUUID())
