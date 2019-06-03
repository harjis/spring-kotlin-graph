package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.Node
import com.example.springkotlingraph.app.exceptions.EntityNotFound
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

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
        val graph = upsertGraph(params.graph)
        upsertNodes(params, graph)
        // Graph and Nodes need to be saved here so that we get primary keys for nodes
        // These are needed so that we can map them to client side keys and remove
        // the nodes which do not exist anymore.
        // Imo. this is a code smell that client has too much responsibility.
        val saved = graphRepository.save(graph)
        mutateGraphSaveParams(params, saved)
        val nodeMap = getNodeMap(params, graph)
        saveEdges(params, graph, nodeMap)
        return saved
    }

    private fun update(params: GraphSaveParams): Graph {
        val graph = upsertGraph(params.graph)
        upsertNodes(params, graph)
        // Graph and Nodes need to be saved here so that we get primary keys for nodes
        // These are needed so that we can map them to client side keys and remove
        // the nodes which do not exist anymore.
        // Imo. this is a code smell that client has too much responsibility.
        val saved = graphRepository.save(graph)
        mutateGraphSaveParams(params, saved)
        val nodeMap = getNodeMap(params, saved)
        deleteNodes(params, graph)
        saveEdges(params, graph, nodeMap)
        deleteEdges(params, graph)
        return graph
    }

    // Ugh... this is just plain hack but I was unable to get anything else to work
    // In order to know which nodes the client still has we need to keep clientId and Id pairs
    // so that we can delete the nodes which client doesn't have anymore. For this we need
    // mutate params after saving has been done so that params have updated primary keys
    private fun mutateGraphSaveParams(params: GraphSaveParams, graph: Graph) {
        graph.nodes.forEach {
            val nodeParam = params.nodes.find { nodeParams -> it.clientId == nodeParams.clientId }
            if (nodeParam is NodeParams) {
                nodeParam.id = it.id
            }
        }
    }

    private fun upsertGraph(graphParams: GraphParams): Graph {
        return if (graphParams.id == null) {
            Graph(name = graphParams.name)
        } else {
            val graph = graphRepository.findById(graphParams.id).orElseThrow { EntityNotFound(graphParams.id) }
            graph.name = graphParams.name
            graph
        }
    }

    private fun upsertNodes(params: GraphSaveParams, graph: Graph) {
        params.nodes.forEach {
            if (it.id == null) {
                Node(name = it.name, graph = graph, clientId = it.clientId)
            } else {
                val persistedNode = graph.nodes.find { node -> node.id == it.id }
                persistedNode!!.name = it.name
            }
        }
    }

    private fun deleteNodes(params: GraphSaveParams, graph: Graph) {
        val persistedNodeIds: List<Long> = graph.nodes.map { it.id }.filterNotNull()
        val newNodeIds = params.nodes.map { it.id }.filterNotNull()
        val toBeDeleted = persistedNodeIds.minus(newNodeIds)
        graph.deleteNodes(toBeDeleted)
    }

    private fun saveEdges(params: GraphSaveParams, graph: Graph, nodeMap: Map<UUID, Node>) {
        params.edges.forEach {
            val fromAndToNode = if (it.fromNodeId is Long && it.toNodeId is Long) {
                val fromNode = graph.nodeById(it.fromNodeId)
                val toNode = graph.nodeById(it.toNodeId)
                Pair(fromNode, toNode)
            } else if (it.fromNodeId is UUID && it.toNodeId is UUID) {
                val fromNode = nodeMap[it.fromNodeId]
                val toNode = nodeMap[it.toNodeId]
                Pair(fromNode, toNode)
            } else if (it.fromNodeId is UUID && it.toNodeId is Long) {
                val fromNode = nodeMap[it.fromNodeId]
                val toNode = graph.nodeById(it.toNodeId)
                Pair(fromNode, toNode)
            } else if (it.fromNodeId is Long && it.toNodeId is UUID) {
                val fromNode = graph.nodeById(it.fromNodeId)
                val toNode = nodeMap[it.toNodeId]
                Pair(fromNode, toNode)
            } else {
                throw Exception("Not gud")
            }

            val (fromNode, toNode) = fromAndToNode
            if (fromNode is Node && toNode is Node) {
                Edge(fromNode = fromNode, toNode = toNode)
            } else {
                throw EntityNotFound("Could not find fromNode or toNode")
            }
        }
    }

    private fun deleteEdges(params: GraphSaveParams, graph: Graph) {
        graph.uniqueEdges().forEach {
            val edgeInParams = params.edges.find { edgeParams -> edgeParams.id == it.id }
            if (edgeInParams == null) {
                graph.removeEdge(it)
            }
        }
    }

    private fun getNodeMap(params: GraphSaveParams, graph: Graph): Map<UUID, Node> {
        return params.nodes
                .mapIndexed { index, nodeParams -> nodeParams.clientId to graph.nodes[index] }.toMap()
    }
}

data class GraphSaveParams(
        val graph: GraphParams,
        val nodes: MutableList<NodeParams> = mutableListOf(),
        val edges: MutableList<EdgeParams> = mutableListOf()
)

data class GraphParams(val id: Long? = null, val name: String)
data class NodeParams(var id: Long? = null, val name: String, val clientId: UUID)
//TODO I would like nodeId's to be string | number
data class EdgeParams(val id: Long? = null, val fromNodeId: Any?, val toNodeId: Any?)
