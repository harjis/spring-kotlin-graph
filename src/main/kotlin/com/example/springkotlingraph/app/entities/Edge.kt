package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = arrayOf(
        UniqueConstraint(columnNames = arrayOf("from_node_id", "to_node_id"))
))
class Edge(
        @Id
        var id: UUID,
        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "from_node_id", nullable = false)
        val fromNode: Node,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "to_node_id", nullable = false)
        val toNode: Node
) {
    init {
        this.fromNode.fromEdges.add(this)
        this.toNode.toEdges.add(this)
    }
}
