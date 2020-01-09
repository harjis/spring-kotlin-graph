package com.example.springkotlingraph.app.views.graph

import com.example.springkotlingraph.app.views.edge.EdgeView
import com.example.springkotlingraph.app.views.node.NodeView

data class GraphDataView(val graph: GraphView, val nodes: List<NodeView>, val edges: List<EdgeView>)
