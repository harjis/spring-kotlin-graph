package com.example.springkotlingraph.services.graph

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.services.graph.GraphSave
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GraphSaveTest {
    @Autowired lateinit var graphSave: GraphSave

    @Test
    fun savesSimpleStructure() {
        val graph = Graph(name = "Graph 1")
        val params = GraphSaveParams(graph = graph)
        val savedGraph = graphSave.save(params)
        println(graph.id)
        println(savedGraph.id)
    }
}
