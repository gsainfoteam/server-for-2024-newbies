package me.gistory.newbies_server_24.controllers

import me.gistory.newbies_server_24.dto.BoardListDto
import me.gistory.newbies_server_24.entities.Board
import me.gistory.newbies_server_24.services.BoardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {
    @GetMapping("")
    fun getBoards(): BoardListDto {
        val boards = boardService.getBoards()
        return BoardListDto(
            count = boards.size,
            list = boards.map(Board::toSummaryDto),
        )
    }
}