package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Node(
        var name: String,
        val clientId: UUID,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "graph_id", nullable = false)
        val graph: Graph,

        @JsonManagedReference
        @OneToMany(mappedBy = "fromNode", cascade = [CascadeType.ALL])
        val fromEdges: MutableList<Edge> = mutableListOf(),

        @JsonManagedReference
        @OneToMany(mappedBy = "toNode", cascade = [CascadeType.ALL])
        val toEdges: MutableList<Edge> = mutableListOf()
) : AbstractJpaPersistable<Long>() {
    init {
        this.graph.nodes.add(this)
    }

    fun removeEdge(edge: Edge) {
        this.fromEdges.removeIf { it.id == edge.id }
        this.toEdges.removeIf { it.id == edge.id }
    }
}
