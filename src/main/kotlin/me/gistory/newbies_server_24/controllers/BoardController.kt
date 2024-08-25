package me.gistory.newbies_server_24.controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import me.gistory.newbies_server_24.dto.BoardListDto
import me.gistory.newbies_server_24.dto.BoardSummaryDto
import me.gistory.newbies_server_24.dto.CreateBoardRequestDto
import me.gistory.newbies_server_24.entities.Board
import me.gistory.newbies_server_24.services.BoardService
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name="Board")
@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @SecurityRequirement(name = "Bearer Authorization")
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
}