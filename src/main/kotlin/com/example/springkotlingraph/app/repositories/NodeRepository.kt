package com.example.springkotlingraph.app.repositories

import com.example.springkotlingraph.app.entities.Node
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NodeRepository : CrudRepository<Node, Long>
