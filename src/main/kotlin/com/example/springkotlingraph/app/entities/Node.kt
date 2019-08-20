package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Suppress("LeakingThis")
@Entity
class Node(
        var name: String,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "graph_id", nullable = false)
        val graph: Graph,

        id: UUID = UUID.randomUUID()
) : AbstractJpaPersistable<UUID>(id) {

    init {
        this.graph.addNode(this)
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "fromNode", cascade = [CascadeType.ALL])
    val fromEdges: MutableSet<Edge> = mutableSetOf()

    @JsonManagedReference
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
