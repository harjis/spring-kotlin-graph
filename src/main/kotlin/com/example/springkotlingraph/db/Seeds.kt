package com.example.springkotlingraph.db

import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.Node
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.util.*
import javax.transaction.Transactional

@Configuration
@Profile("dev")
class Seeds {
    @Bean
    @Transactional
    fun initDatabase(graphRepository: GraphRepository) = CommandLineRunner {
        val graph = Graph(id = UUID.randomUUID(), name = "Graph 1")
        val node1 = Node(id = UUID.randomUUID(), name = "Node 1", graph = graph)
        val node2 = Node(id = UUID.randomUUID(), name = "Node 2", graph = graph)
        Edge(id = UUID.randomUUID(), fromNode = node1, toNode = node2)
        graphRepository.save(graph)
    }
}
