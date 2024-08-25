package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.CreateBoardRequestDto
import me.gistory.newbies_server_24.entities.Board
import me.gistory.newbies_server_24.entities.User
import me.gistory.newbies_server_24.exceptions.UserNotFoundException
import me.gistory.newbies_server_24.repositories.AuthRepository
import me.gistory.newbies_server_24.repositories.BoardRepository
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class BoardService(
    private val boardRepository: BoardRepository,
    private val authRepository: AuthRepository,
) {
    fun getBoards() = boardRepository.findAll().toList()

    fun createBoard(body: CreateBoardRequestDto, userEmail: String): Board {
        val user = authRepository.findByEmail(userEmail)
            ?: throw UserNotFoundException()
        return boardRepository.save(Board(title = body.title, createdBy = user))
    }
}