package com.example.springkotlingraph.app.entities.nodes

import com.example.springkotlingraph.app.entities.Graph
import javax.persistence.Entity

@Entity
class OutputNode(
        graph: Graph,
        name: String,
        x: Float,
        y: Float
) : Node(graph, name, x, y) {
}
