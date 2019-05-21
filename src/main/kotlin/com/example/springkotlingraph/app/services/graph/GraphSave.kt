package com.example.springkotlingraph.app.services.graph

import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.entities.Node
import com.example.springkotlingraph.app.repositories.GraphRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GraphSave(private val graphRepository: GraphRepository) {
    fun save(params: GraphSaveParams): Graph {
        if (params.graph.id != null) {
            val graph = graphRepository.findById(params.graph.id)
        } else {
            return graphRepository.save(params.graph.toGraph())
        }

    }

}

class GraphSaveParams(val graph: GraphParams)
class GraphParams(val id: Long? = null, val name: String) {
    fun toGraph(): Graph {
        return Graph(name = this.name)
    }
}
