package com.example.springkotlingraph.app.views.node

import java.util.*

data class NodeView(
        val content: String = "", // TODO Make me JSON object
        val graph_id: UUID,
        val id: UUID,
        val name: String,
        val to_edge_ids: List<String> = listOf(),
        val type: String = "InputNode",
        val x: Float,
        val y: Float
)
