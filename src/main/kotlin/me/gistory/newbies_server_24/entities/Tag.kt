package me.gistory.newbies_server_24.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tags")
class Tag (
    @Id
    val key: String
) {}