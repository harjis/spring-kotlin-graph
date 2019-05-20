package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Node(
        val name: String,
        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "graph_id", nullable = false)
        val graph: Graph
) : AbstractJpaPersistable<Long>() {
    init {
        this.graph.nodes.add(this)
    }
}
