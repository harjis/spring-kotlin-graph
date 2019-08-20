package com.example.springkotlingraph.app.views.edge

import java.util.*

data class EdgeView(
        val from_node_id: UUID,
        val id: UUID,
        val to_node_id: UUID
)
