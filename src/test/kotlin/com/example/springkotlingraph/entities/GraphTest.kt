package com.example.springkotlingraph.entities

import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.nodes.Node
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class GraphTest(
        @Autowired val graphRepository: GraphRepository
) {
    @Test
    @Transactional // Wether or not this should be transactional is unknown
    fun testSomething() {
        val graph = Graph(name = "Graph 1")
        val node1 = Node(name = "Node 1", graph = graph, x = 0F, y = 0F)
        val node2 = Node(name = "Node 2", graph = graph, x = 0F, y = 0F)
        Edge(fromNode = node1, toNode = node2)
        graphRepository.save(graph)

        val graphs = graphRepository.findAll()
        Assertions.assertThat(graphs.count()).isEqualTo(1)
        val fetchedGraph = graphs.first()
        Assertions.assertThat(fetchedGraph.nodes.count()).isEqualTo(2)
        val firstNode = graph.nodes.first()
        Assertions.assertThat(firstNode.fromEdges.count()).isEqualTo(1)
    }
}
