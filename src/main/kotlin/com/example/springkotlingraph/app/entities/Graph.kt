package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Graph(
        @Id
        var id: UUID,
        var name: String,
        @JsonManagedReference
        @OneToMany(mappedBy = "graph", cascade = [CascadeType.ALL])
        val nodes: MutableSet<Node> = mutableSetOf()
) {

    fun nodeById(nodeId: UUID): Node? {
        return this.nodes.find { it.id == nodeId }
    }

    fun deleteNodes(nodeIds: List<UUID>) {
        this.nodes.removeIf { nodeIds.contains(it.id) }
    }

    fun uniqueEdges(): MutableSet<Edge> {
        return this.nodes.flatMap { it.fromEdges.union(it.toEdges) }.distinctBy { it.id }.toMutableSet()
    }

    fun removeEdge(edge: Edge) {
        this.nodes.map { it.removeEdge(edge) }
    }
}
