package me.gistory.newbies_server_24.dto

import java.util.*

data class BoardSummaryDto(
    val id: UUID,
    val title: String,
    val createdAt: Date,
    val creator: UserDto,
)

data class BoardListDto(
    val count: Int,
    val list: List<BoardSummaryDto>,
)

data class CreateBoardRequestDto(
    val title: String,
)