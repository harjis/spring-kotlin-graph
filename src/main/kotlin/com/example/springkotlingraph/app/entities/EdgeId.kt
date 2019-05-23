package com.example.springkotlingraph.app.entities

import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class EdgeId(
        @Column(name = "from_node_id")
        val fromNodeId: Long,
        @Column(name = "to_node_id")
        val toNodeId: Long
) : Serializable {

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as EdgeId

        return fromNodeId == other.fromNodeId && toNodeId == other.toNodeId
    }

    override fun hashCode(): Int {
        return 31
    }
}
