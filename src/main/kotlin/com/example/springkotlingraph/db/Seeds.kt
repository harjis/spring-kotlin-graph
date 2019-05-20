package com.example.springkotlingraph.db

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.Node
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.transaction.Transactional

@Configuration
class Seeds {
    @Bean
    @Transactional
    fun initDatabase(graphRepository: GraphRepository) = CommandLineRunner {
        val graph = Graph(name = "Graph 1")
        val node1 = Node(name = "Node 1", graph = graph)
        val nod2 = Node(name = "Node 2", graph = graph)
        graphRepository.save(graph)
    }
}
