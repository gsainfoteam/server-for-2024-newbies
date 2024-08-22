package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AuthRepository: CrudRepository<User, UUID> {
    fun findByEmail(email: String): User?
}