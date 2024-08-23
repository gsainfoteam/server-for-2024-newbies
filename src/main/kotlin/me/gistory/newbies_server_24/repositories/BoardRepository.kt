package me.gistory.newbies_server_24.repositories

import me.gistory.newbies_server_24.entities.Board
import org.springframework.data.repository.CrudRepository
import java.util.*

interface BoardRepository : CrudRepository<Board, UUID>