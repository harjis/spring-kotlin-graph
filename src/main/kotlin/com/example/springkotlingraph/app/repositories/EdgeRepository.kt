package com.example.springkotlingraph.app.repositories

import com.example.springkotlingraph.app.entities.Edge
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EdgeRepository : CrudRepository<Edge, Long>
