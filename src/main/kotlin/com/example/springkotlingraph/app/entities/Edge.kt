package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Edge(
        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "from_node_id", nullable = false)
        val fromNode: Node,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "to_node_id", nullable = false)
        val toNode: Node
) : AbstractJpaPersistable<Long>() {
    init {
        this.fromNode.fromEdges.add(this)
        this.toNode.toEdges.add(this)
    }
}
