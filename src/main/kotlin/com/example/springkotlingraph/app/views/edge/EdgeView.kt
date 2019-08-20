package com.example.springkotlingraph.app.views.edge

import java.util.*

data class EdgeView(
        val fromNodeId: UUID,
        val id: UUID,
        val toNodeId: UUID
)
