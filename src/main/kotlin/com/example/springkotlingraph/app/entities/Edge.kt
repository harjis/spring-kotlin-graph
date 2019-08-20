package com.example.springkotlingraph.app.entities

import com.example.springkotlingraph.app.entities.nodes.Node
import com.example.springkotlingraph.app.views.edge.EdgeView
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("from_node_id", "to_node_id"))])
class Edge(
        @ManyToOne
        @JoinColumn(name = "from_node_id", nullable = false)
        val fromNode: Node,

        @ManyToOne
        @JoinColumn(name = "to_node_id", nullable = false)
        val toNode: Node,

        id: UUID = UUID.randomUUID()
) : AbstractJpaPersistable<UUID>(id) {
    init {
        this.fromNode.addFromEdge(this)
        this.toNode.addToEdge(this)
    }
}

fun Edge.render() = EdgeView(
        fromNodeId = this.fromNode.id,
        id = this.id,
        toNodeId = this.toNode.id
)
