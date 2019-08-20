package com.example.springkotlingraph.db

import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.nodes.InputNode
import com.example.springkotlingraph.app.entities.nodes.OutputNode
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.transaction.Transactional

@Configuration
@Profile("dev")
class Seeds {
    @Bean
    @Transactional
    fun initDatabase(graphRepository: GraphRepository) = CommandLineRunner {
        if (graphRepository.findAll().count() > 0) {
            return@CommandLineRunner
        }
        val graph = Graph(name = "Graph 1")
        val node1 = InputNode(name = "InputNode 1", graph = graph, x = 50F, y = 200F)
        val node2 = OutputNode(name = "OutputNode 1", graph = graph, x = 300F, y = 400F)
        Edge(fromNode = node1, toNode = node2)
        graphRepository.save(graph)

        val graph2 = Graph(name = "Graph 2")
        val node21 = InputNode(name = "InputNode 2", graph = graph2, x = 10F, y = 10F)
        val node22 = OutputNode(name = "OutputNode 2", graph = graph2, x = 400F, y = 200F)
        Edge(fromNode = node21, toNode = node22)
        graphRepository.save(graph2)
    }
}
