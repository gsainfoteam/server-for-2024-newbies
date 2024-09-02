package me.gistory.newbies_server_24.dto

import java.util.*

data class UserDto(
    val id: UUID,
    val email: String,
    val nickname: String,
    val createdAt: Date,
)