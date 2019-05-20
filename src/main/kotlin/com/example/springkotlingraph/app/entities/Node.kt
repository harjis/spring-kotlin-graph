package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Node(
        val name: String,

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
) : AbstractJpaPersistable<Long>() {
    init {
        this.graph.nodes.add(this)
    }
}