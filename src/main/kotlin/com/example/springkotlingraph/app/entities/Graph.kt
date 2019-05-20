package com.example.springkotlingraph.app.entities

import javax.persistence.Entity

@Entity
class Graph(val name: String) : AbstractJpaPersistable<Long>()
