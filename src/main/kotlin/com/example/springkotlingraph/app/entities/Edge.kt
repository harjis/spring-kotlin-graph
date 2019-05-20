package com.example.springkotlingraph.app.entities

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Edge(
        @ManyToOne
        @JoinColumn(name = "from_node_id", nullable = false)
        val fromNode: Node,
        @ManyToOne
        @JoinColumn(name = "to_node_id", nullable = false)
        val toNode: Node
) : AbstractJpaPersistable<Long>() {
    init {
        this.fromNode.fromEdges.add(this)
        this.toNode.toEdges.add(this)
    }
}
