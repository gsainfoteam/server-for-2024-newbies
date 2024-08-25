package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.Post
import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, Long>