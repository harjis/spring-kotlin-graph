package com.example.springkotlingraph.services.graph

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.services.graph.GraphSave
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GraphSaveTest {
    @Autowired lateinit var graphRepository: GraphRepository
    @Autowired lateinit var graphSave: GraphSave

    @Test
    fun savesSimpleStructure() {
        val graph = Graph(name = "Graph 1")
        val params = GraphSaveParams(graph = graph)
        graphSave.save(params)
        Assertions.assertThat(graphRepository.findAll().count()).isEqualTo(1);
    }
}
