package com.example.springkotlingraph.app.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Persistable
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient
import javax.persistence.Version

/**
 * Abstract base class for entities. Allows parameterization of id type, chooses auto-generation and implements
 * [equals] and [hashCode] based on that id.
 *
 * This class was inspired by [org.springframework.data.jpa.domain.AbstractPersistable], which is part of the Spring Data project.
 */
@MappedSuperclass
abstract class AbstractJpaPersistable<T : Serializable>(
        @Id
        private val id: T
) : Persistable<T> {
    @Version
    private val version: Long? = null

    override fun getId(): T = id

    /**
     * Must be [Transient] in order to ensure that no JPA provider complains because of a missing setter.
     *
     * @see org.springframework.data.domain.Persistable.isNew
     */
    @Transient
    @JsonIgnore
    override fun isNew() = version != null

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as AbstractJpaPersistable<*>

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }
}
