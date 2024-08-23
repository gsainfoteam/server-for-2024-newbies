package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.LoginRequestDto
import me.gistory.newbies_server_24.dto.RegisterRequestDto
import me.gistory.newbies_server_24.entities.User
import me.gistory.newbies_server_24.exceptions.UserAlreadyExistException
import me.gistory.newbies_server_24.exceptions.UserNotFoundException
import me.gistory.newbies_server_24.repositories.AuthRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class AuthService(private val authRepository: AuthRepository, private val passwordEncoder: PasswordEncoder) {
    fun login(loginRequestDto: LoginRequestDto) {
        val user = authRepository.findByEmail(loginRequestDto.email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(loginRequestDto.password, user.password)) {
            throw UserNotFoundException()
        }
    }
    fun register(registerRequestDto: RegisterRequestDto) {
        authRepository.findByEmail(registerRequestDto.email)?.let { throw UserAlreadyExistException() }
        val user = User(
            email = registerRequestDto.email,
            password =  passwordEncoder.encode(registerRequestDto.password)
        ).also { authRepository.save(it) }
    }
}