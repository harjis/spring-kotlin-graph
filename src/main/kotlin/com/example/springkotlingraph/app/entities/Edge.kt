package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Edge(
        @EmbeddedId
        val id: EdgeId,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "from_node_id", nullable = false, insertable = false, updatable = false)
        val fromNode: Node,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "to_node_id", nullable = false, insertable = false, updatable = false)
        val toNode: Node
){
    init {
        this.fromNode.fromEdges.add(this)
        this.toNode.toEdges.add(this)
    }
}
