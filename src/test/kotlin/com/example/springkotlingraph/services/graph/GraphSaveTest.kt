package com.example.springkotlingraph.services.graph

import com.example.springkotlingraph.app.repositories.EdgeRepository
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.repositories.NodeRepository
import com.example.springkotlingraph.app.services.graph.EdgeParams
import com.example.springkotlingraph.app.services.graph.GraphParams
import com.example.springkotlingraph.app.services.graph.GraphSave
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
import com.example.springkotlingraph.app.services.graph.NodeParams
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@SpringBootTest
class GraphSaveTest {
    @Autowired
    lateinit var graphRepository: GraphRepository
    @Autowired
    lateinit var graphSave: GraphSave
    @Autowired
    lateinit var edgeRepository: EdgeRepository
    @Autowired
    lateinit var nodeRepository: NodeRepository

    @Test
    fun canSaveGraph() {
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"))
        graphSave.save(params)
        Assertions.assertThat(graphRepository.findAll().count()).isEqualTo(1)
    }

    @Test
    fun canUpdateGraph() {
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"))
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1")
        )
        graphSave.save(updateParams)
        Assertions.assertThat(graphRepository.findAll().count()).isEqualTo(1)
        val updatedGraph = graphRepository.findAll().first()
        Assertions.assertThat(updatedGraph.name).isEqualTo("Updated Graph 1")
    }

    @Test
    fun canSaveGraphNodes() {
        val nodesParams = mutableListOf(
                NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                NodeParams(name = "Node 2", clientId = UUID.randomUUID())
        )
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        graphSave.save(params)
        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val savedGraph = graphs.first()
        Assertions.assertThat(savedGraph.nodes.count()).isEqualTo(2)
    }

    @Test
    fun canUpdateGraphNodes() {
        val nodesParams = mutableListOf(
                NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                NodeParams(name = "Node 2", clientId = UUID.randomUUID())
        )
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId)
                }.toMutableList()
        )
        graphSave.save(updateParams)
        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val graph = graphs.first()
        Assertions.assertThat(graph.name).isEqualTo("Updated Graph 1")
        Assertions.assertThat(graph.nodes.count()).isEqualTo(2)
        Assertions.assertThat(graph.nodes.first().name).isEqualTo("Updated Node 1")
        Assertions.assertThat(graph.nodes.last().name).isEqualTo("Updated Node 2")
    }

    @Test
    fun canRemoveNodes() {
        val nodesParams = mutableListOf(
                NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                NodeParams(name = "Node 2", clientId = UUID.randomUUID())
        )
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes
                        .map {
                            NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId)
                        }.toMutableList()
        )
        graphSave.save(updateParams)
        val deleteParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.take(1).map {
                    NodeParams(id = it.id, name = it.name, clientId = it.clientId)
                }.toMutableList()
        )
        graphSave.save(deleteParams)
        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val graph = graphs.first()
        Assertions.assertThat(graph.name).isEqualTo("Updated Graph 1")
        Assertions.assertThat(graph.nodes.count()).isEqualTo(1)
        Assertions.assertThat(graph.nodes.first().name).isEqualTo("Updated Node 1")
    }

    @Test
    fun canAddAndUpdateGraphNodes() {
        val params = GraphSaveParams(
                graph = GraphParams(name = "Graph 1"),
                nodes = mutableListOf(
                        NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                        NodeParams(name = "Node 2", clientId = UUID.randomUUID())
                )
        )
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes
                        .map { NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId) }
                        .plus(NodeParams(name = "New Node 3", clientId = UUID.randomUUID()))
                        .toMutableList()
        )
        graphSave.save(updateParams)
        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val graph = graphs.first()
        Assertions.assertThat(graph.name).isEqualTo("Updated Graph 1")
        val edges = edgeRepository.findAll()
        val nodes = nodeRepository.findAll()
        Assertions.assertThat(nodes.count()).isEqualTo(3)

        Assertions.assertThat(graph.nodes.count()).isEqualTo(3)
        Assertions.assertThat(graph.nodes.first().name).isEqualTo("Updated Node 1")
        Assertions.assertThat(graph.nodes.elementAt(1).name).isEqualTo("Updated Node 2")
        Assertions.assertThat(graph.nodes.last().name).isEqualTo("New Node 3")
    }

    @Test
    fun canSaveEdges() {
        val nodesParams = mutableListOf(
                NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                NodeParams(name = "Node 2", clientId = UUID.randomUUID())
        )
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId)
                }.toMutableList(),
                edges = mutableListOf(
                        EdgeParams(
                                fromNodeId = savedGraph.nodes.first().id!!.toLong(),
                                toNodeId = savedGraph.nodes.last().id!!.toLong()
                        )
                )
        )
        graphSave.save(updateParams)
        val graphs = graphRepository.findAll()
        val graph = graphs.first()
        Assertions.assertThat(graph.uniqueEdges().count()).isEqualTo(1)
    }

    @Test
    fun canRemoveEdges() {
        val nodesParams = mutableListOf(
                NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                NodeParams(name = "Node 2", clientId = UUID.randomUUID())
        )
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId)
                }.toMutableList(),
                edges = mutableListOf(
                        EdgeParams(
                                fromNodeId = savedGraph.nodes.first().id!!.toLong(),
                                toNodeId = savedGraph.nodes.last().id!!.toLong()
                        )
                )
        )
        graphSave.save(updateParams)
        val deleteParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId)
                }.toMutableList(),
                edges = mutableListOf()
        )
        graphSave.save(deleteParams)
        val graphs = graphRepository.findAll()
        val graph = graphs.first()
        Assertions.assertThat(graph.uniqueEdges().count()).isEqualTo(0)
    }

    @Test
    fun canNotCreateDuplicateEdges() {
        val nodesParams = mutableListOf(
                NodeParams(name = "Node 1", clientId = UUID.randomUUID()),
                NodeParams(name = "Node 2", clientId = UUID.randomUUID())
        )
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        var savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name, clientId = it.clientId)
                }.toMutableList(),
                edges = mutableListOf(
                        EdgeParams(
                                fromNodeId = savedGraph.nodes.first().id!!.toLong(),
                                toNodeId = savedGraph.nodes.last().id!!.toLong()
                        ),
                        EdgeParams(
                                fromNodeId = savedGraph.nodes.first().id!!.toLong(),
                                toNodeId = savedGraph.nodes.last().id!!.toLong()
                        )
                )
        )
        graphSave.save(updateParams)
        // Doesn't really make sense to me. Imo data integrity expection should be thrown with
        // graphRepository.findAll too. I would have expected that getting the record again would
        // return persisted data but it seems that it doesnt do that
//        assertThrows<DataIntegrityViolationException> { graphRepository.findAll() }
        assertThrows<DataIntegrityViolationException> { edgeRepository.findAll() }
    }

    @Test
    fun canSaveAllAtOnce() {
        val nodeId = UUID.randomUUID()
        val nodeId2 = UUID.randomUUID()
        val params = GraphSaveParams(
                graph = GraphParams(name = "Graph 1"),
                nodes = mutableListOf(
                        NodeParams(name = "Node 1", clientId = nodeId),
                        NodeParams(name = "Node 2", clientId = nodeId2)
                ),
                edges = mutableListOf(
                        EdgeParams(fromNodeId = nodeId, toNodeId = nodeId2)
                )
        )
        val savedGraph = graphSave.save(params)
        Assertions.assertThat(savedGraph.nodes.count()).isEqualTo(2)
        Assertions.assertThat(savedGraph.uniqueEdges().count()).isEqualTo(1)
    }
}
