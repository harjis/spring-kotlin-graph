package com.example.springkotlingraph.app.repositories

import com.example.springkotlingraph.app.entities.nodes.Node
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NodeRepository : ConnectCrudRepository<Node, UUID>
