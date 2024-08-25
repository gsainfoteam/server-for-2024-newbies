package me.gistory.newbies_server_24.dto

import me.gistory.newbies_server_24.entities.Board
import java.util.*

data class CreatePostDto(
    val title: String,
    val body: String,
    val tags: List<String>
)

data class UpdatePostDto(
    val title: String?,
    val body: String?,
    val tags: List<String>?,
)

data class PostDto (
    val id: UUID,
    val title: String,
    val body: String,
    val tags: List<String>,
    val board: BoardSummaryDto,
    val createdAt: Date,
    val createdBy: UserDto
)

data class PostListDto (
    val count: Int,
    val list: List<PostDto>,
)
