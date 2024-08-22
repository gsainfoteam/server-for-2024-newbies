package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.LoginRequestDto
import me.gistory.newbies_server_24.dto.RegisterRequestDto
import me.gistory.newbies_server_24.exceptions.UserNotFoundException
import me.gistory.newbies_server_24.repositories.AuthRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
@Transactional
class AuthService(private val authRepository: AuthRepository) {
    fun login(loginRequestDto: LoginRequestDto) {
        val user = authRepository.findByEmail(loginRequestDto.email) ?: throw UserNotFoundException()
        BCrypt.checkpw(loginRequestDto.password, user.password)
    }
    fun register(registerRequestDto: RegisterRequestDto) = ""
}