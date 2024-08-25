package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository: CrudRepository<RefreshToken, String> {
}