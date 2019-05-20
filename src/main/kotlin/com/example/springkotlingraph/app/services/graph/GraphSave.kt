package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.stereotype.Service

@Service
class GraphSave(val graphRepository: GraphRepository) {
    fun save(params: SaveParams) {
        graphRepository.save(params.graph)
    }
}

data class SaveParams(val graph: Graph)
