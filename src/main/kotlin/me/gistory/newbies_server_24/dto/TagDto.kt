package me.gistory.newbies_server_24.dto

data class TagDto(
    val key: String
)

data class TagListDto (
    val count: Int,
    val list: List<TagDto>
)