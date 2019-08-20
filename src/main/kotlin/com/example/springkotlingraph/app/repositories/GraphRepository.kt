package com.example.springkotlingraph.app.repositories

import com.example.springkotlingraph.app.entities.Graph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GraphRepository : ConnectCrudRepository<Graph, UUID>
