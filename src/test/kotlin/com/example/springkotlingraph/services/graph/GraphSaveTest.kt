package com.example.springkotlingraph.services.graph

import com.example.springkotlingraph.app.repositories.GraphRepository
import com.example.springkotlingraph.app.services.graph.GraphParams
import com.example.springkotlingraph.app.services.graph.GraphSave
import com.example.springkotlingraph.app.services.graph.GraphSaveParams
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

    @Transactional
    @Test
    fun savesGraphsNodes() {
    }
}
