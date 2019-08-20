package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Suppress("LeakingThis")
@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("from_node_id", "to_node_id"))])
class Edge(
        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "from_node_id", nullable = false)
        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
        @JsonIdentityReference(alwaysAsId=true)
        val fromNode: Node,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "to_node_id", nullable = false)
        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
        @JsonIdentityReference(alwaysAsId=true)
        val toNode: Node,

        id: UUID = UUID.randomUUID()
) : AbstractJpaPersistable<UUID>(id) {
    init {
        this.fromNode.addFromEdge(this)
        this.toNode.addToEdge(this)
    }
}
