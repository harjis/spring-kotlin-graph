package com.example.springkotlingraph.app.entities.nodes

import com.example.springkotlingraph.app.entities.AbstractJpaPersistable
import com.example.springkotlingraph.app.entities.Edge
import com.example.springkotlingraph.app.entities.Graph
import com.example.springkotlingraph.app.views.node.NodeView
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

    fun removeEdgeIf(block: (Edge) -> Boolean) {
        this.fromEdges.removeIf(block)
        this.toEdges.removeIf(block)
    }
}

fun Node.render() = NodeView(
        graphId = this.graph.id,
        id = this.id,
        name = this.name,
        toEdgeIds = this.toEdges.map { it.id },
        type = this::class.simpleName!!,//TODO is there a better way than this?
        x = this.x,
        y = this.y
)
