package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Node(
        @Id
        var id: UUID,
        var name: String,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "graph_id", nullable = false)
        val graph: Graph,

        @JsonManagedReference
        @OneToMany(mappedBy = "fromNode", cascade = [CascadeType.ALL])
        val fromEdges: MutableSet<Edge> = mutableSetOf(),

        @JsonManagedReference
        @OneToMany(mappedBy = "toNode", cascade = [CascadeType.ALL])
        val toEdges: MutableSet<Edge> = mutableSetOf()
) {
    init {
        this.graph.nodes.add(this)
    }

    fun removeEdge(edge: Edge) {
        this.fromEdges.removeIf { it.id == edge.id }
        this.toEdges.removeIf { it.id == edge.id }
    }
}
