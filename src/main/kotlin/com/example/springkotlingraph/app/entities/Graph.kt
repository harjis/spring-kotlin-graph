package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Graph(
        var name: String,
        id: UUID = UUID.randomUUID()
) : AbstractJpaPersistable<UUID>(id) {

    @JsonManagedReference
    @OneToMany(mappedBy = "graph", cascade = [CascadeType.ALL], orphanRemoval = true)
    val nodes: MutableSet<Node> = mutableSetOf()

    fun addNode(node: Node) {
        nodes.add(node)
    }

    fun nodeById(nodeId: UUID): Node? {
        return this.nodes.find { it.id == nodeId }
    }

    fun updateNodes(block: (Node) -> Unit) {
        nodes.map(block)
    }

    @JsonGetter
    fun uniqueEdges(): List<Edge> {
        return this.nodes.flatMap { it.fromEdges.union(it.toEdges) }.distinctBy { it.id }
    }

    fun removeNodeIf(block: (Node) -> Boolean) {
        this.nodes.removeIf(block)
    }

    fun removeEdgeIf(block: (Edge) -> Boolean) {
        this.nodes.map { it.removeEdgeIf(block) }
    }
}
