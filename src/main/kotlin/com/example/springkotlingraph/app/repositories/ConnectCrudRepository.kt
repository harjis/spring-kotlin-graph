package com.example.springkotlingraph.app.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface ConnectCrudRepository<T, ID> : CrudRepository<T, ID> {
    /**
     * JPA's existing [CrudRepository.findById] returns an [Optional]. There is no reason to use Optionals in
     * Kotlin, so let's add this nullable version.
     */
    fun findOneById(id: ID): T?
}
