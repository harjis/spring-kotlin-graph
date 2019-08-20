package com.example.springkotlingraph.app.repositories

import com.example.springkotlingraph.app.entities.Edge
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EdgeRepository : ConnectCrudRepository<Edge, UUID>
