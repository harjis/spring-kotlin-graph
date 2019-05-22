package com.example.springkotlingraph.app.exceptions

import java.lang.RuntimeException


class EntityNotFound : RuntimeException {
    constructor(id: Long) : super("Could not find entity with id: $id")
}
