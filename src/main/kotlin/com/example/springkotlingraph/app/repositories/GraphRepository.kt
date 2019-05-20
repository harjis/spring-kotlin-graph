package com.example.springkotlingraph.app.repositories

import com.example.springkotlingraph.app.entities.Graph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GraphRepository : CrudRepository<Graph, Long>
