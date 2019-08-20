package com.example.springkotlingraph.app.views.node

import java.util.*

data class NodeView(
        val content: String = "", // TODO Make me JSON object
        val graphId: UUID,
        val id: UUID,
        val name: String,
        val toEdgeIds: List<String> = listOf(),
        val type: String = "InputNode",
        val x: Float,
        val y: Float
)
