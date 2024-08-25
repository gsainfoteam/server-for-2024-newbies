package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.Tag
import org.springframework.data.repository.CrudRepository

interface TagRepositoy : CrudRepository<Tag, String> {}