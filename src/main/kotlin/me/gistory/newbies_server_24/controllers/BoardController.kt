package me.gistory.newbies_server_24.controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import me.gistory.newbies_server_24.dto.BoardListDto
import me.gistory.newbies_server_24.dto.BoardSummaryDto
import me.gistory.newbies_server_24.dto.CreateBoardRequestDto
import me.gistory.newbies_server_24.entities.Board
import me.gistory.newbies_server_24.services.BoardService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name="Board")
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

    @SecurityRequirement(name = "Bearer Authorization")
    @PostMapping("")
    fun createBoard(
        authentication: Authentication,
        @RequestBody body: CreateBoardRequestDto
    ): BoardSummaryDto {
        return boardService.createBoard(body, authentication.name).toSummaryDto();
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @DeleteMapping("{uuid}")
    fun deleteBoard(
        authentication: Authentication,
        @PathVariable("uuid") uuid: String
    ): Unit {
        return boardService.deleteBoard(UUID.fromString(uuid), authentication.name)
    }
}