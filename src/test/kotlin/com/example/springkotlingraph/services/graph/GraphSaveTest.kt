package com.example.springkotlingraph.services.graph

import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.services.graph.EdgeParams
import com.example.springkotlingraph.app.services.graph.GraphParams
import com.example.springkotlingraph.app.services.graph.GraphSave
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
import com.example.springkotlingraph.app.services.graph.NodeParams
import org.assertj.core.api.Assert
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class GraphSaveTest {
    @Autowired
    lateinit var graphRepository: GraphRepository
    @Autowired
    lateinit var graphSave: GraphSave

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
        val nodesParams = mutableSetOf(NodeParams(name = "Node 1"), NodeParams(name = "Node 2"))
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        graphSave.save(params)
        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val savedGraph = graphs.first()
        Assertions.assertThat(savedGraph.nodes.count()).isEqualTo(2)
    }

    @Test
    fun canUpdateGraphNodes() {
        val nodesParams = mutableSetOf(NodeParams(name = "Node 1"), NodeParams(name = "Node 2"))
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name)
                }.toMutableSet()
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
        val nodesParams = mutableSetOf(NodeParams(name = "Node 1"), NodeParams(name = "Node 2"))
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map { NodeParams(id = it.id, name = "Updated " + it.name) }.toMutableSet()
        )
        graphSave.save(updateParams)
        val deleteParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.take(1).map {
                    NodeParams(id = it.id, name = it.name)
                }.toMutableSet()
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
                nodes = mutableSetOf(NodeParams(name = "Node 1"), NodeParams(name = "Node 2"))
        )
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes
                        .map { NodeParams(id = it.id, name = "Updated " + it.name) }
                        .plus(NodeParams(name = "New Node 3"))
                        .toMutableSet()
        )
        graphSave.save(updateParams)
        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val graph = graphs.first()
        Assertions.assertThat(graph.name).isEqualTo("Updated Graph 1")
        Assertions.assertThat(graph.nodes.count()).isEqualTo(3)
        Assertions.assertThat(graph.nodes.first().name).isEqualTo("Updated Node 1")
        Assertions.assertThat(graph.nodes.elementAt(1).name).isEqualTo("Updated Node 2")
        Assertions.assertThat(graph.nodes.last().name).isEqualTo("New Node 3")
    }

    @Test
    fun canSaveEdges() {
        val nodesParams = mutableSetOf(NodeParams(name = "Node 1"), NodeParams(name = "Node 2"))
        val params = GraphSaveParams(graph = GraphParams(name = "Graph 1"), nodes = nodesParams)
        val savedGraph = graphSave.save(params)
        val updateParams = GraphSaveParams(
                graph = GraphParams(id = savedGraph.id, name = "Updated Graph 1"),
                nodes = savedGraph.nodes.map {
                    NodeParams(id = it.id, name = "Updated " + it.name)
                }.toMutableSet(),
                edges = mutableSetOf(
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
}
