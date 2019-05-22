package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Graph(
        var name: String,
        @JsonManagedReference
        @OneToMany(mappedBy = "graph", cascade = [CascadeType.ALL])
        val nodes: MutableSet<Node> = mutableSetOf()
) : AbstractJpaPersistable<Long>()
