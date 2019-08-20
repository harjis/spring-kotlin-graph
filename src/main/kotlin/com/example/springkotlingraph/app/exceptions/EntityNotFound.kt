package com.example.springkotlingraph.app.exceptions

import java.lang.RuntimeException
import java.util.*


class EntityNotFound : RuntimeException {
    constructor(id: UUID) : super("Could not find entity with id: $id")
    constructor(message: String) : super(message)
}
