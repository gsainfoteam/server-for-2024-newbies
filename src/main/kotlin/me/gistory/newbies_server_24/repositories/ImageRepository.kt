package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.Image
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ImageRepository : CrudRepository<Image, UUID> {
}