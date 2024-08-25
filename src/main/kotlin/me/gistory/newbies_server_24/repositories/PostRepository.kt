package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.Board
import me.gistory.newbies_server_24.entities.Post
import me.gistory.newbies_server_24.entities.Tag
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PostRepository : CrudRepository<Post, UUID> {
    fun findByTags(tag: Tag): List<Post>
    fun findByBoard(board: Board): List<Post>
    fun findByTagsAndBoard(tag: Tag, board: Board): List<Post>
    fun findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(titleContainingIgnoreCase: String, bodyContainingIgnoreCase: String): List<Post>
}