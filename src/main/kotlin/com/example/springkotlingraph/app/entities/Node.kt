package com.example.springkotlingraph.app.entities

import com.example.springkotlingraph.app.views.node.NodeView
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Node(
        @ManyToOne
        @JoinColumn(name = "graph_id", nullable = false)
        val graph: Graph,

        var name: String,
        var x: Float,
        var y: Float,

        id: UUID = UUID.randomUUID()
) : AbstractJpaPersistable<UUID>(id) {

    init {
        this.graph.addNode(this)
    }

    @OneToMany(mappedBy = "fromNode", cascade = [CascadeType.ALL])
    val fromEdges: MutableSet<Edge> = mutableSetOf()

    @OneToMany(mappedBy = "toNode", cascade = [CascadeType.ALL])
    val toEdges: MutableSet<Edge> = mutableSetOf()

    fun addFromEdge(edge: Edge) {
        this.fromEdges.add(edge)
    }

    fun addToEdge(edge: Edge) {
        this.toEdges.add(edge)
    }

    fun removeEdge(edge: Edge) {
        this.fromEdges.removeIf { it.id == edge.id }
        this.toEdges.removeIf { it.id == edge.id }
    }

    fun removeEdgeIf(block: (Edge) -> Boolean) {
        this.fromEdges.removeIf(block)
        this.toEdges.removeIf(block)
    }
}

fun Node.render() = NodeView(
        graph_id = this.graph.id,
        id = this.id,
        name = this.name,
        x = this.x,
        y = this.y
)
