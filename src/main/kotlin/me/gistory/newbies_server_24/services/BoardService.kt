package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.repositories.BoardRepository
import org.springframework.stereotype.Service

@Service
@Transactional
class BoardService(
    private val boardRepository: BoardRepository
) {
    fun getBoards() = boardRepository.findAll().toList()
}