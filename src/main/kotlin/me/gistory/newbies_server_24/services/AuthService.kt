package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.LoginRequestDto
import me.gistory.newbies_server_24.dto.RegisterRequestDto
import me.gistory.newbies_server_24.entities.User
import me.gistory.newbies_server_24.exceptions.UserAlreadyExistException
import me.gistory.newbies_server_24.exceptions.UserNotFoundException
import me.gistory.newbies_server_24.providers.TokenProvider
import me.gistory.newbies_server_24.repositories.AuthRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class AuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun login(loginRequestDto: LoginRequestDto): String {
        val user = authRepository.findByEmail(loginRequestDto.email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(loginRequestDto.password, user.password)) {
            throw UserNotFoundException()
        }
        return generateToken(user)
    }
    fun register(registerRequestDto: RegisterRequestDto): String {
        logger.info("register with email: `{}`", registerRequestDto.email)
        authRepository.findByEmail(registerRequestDto.email)?.let { throw UserAlreadyExistException() }
        val user = User(
            email = registerRequestDto.email,
            password =  passwordEncoder.encode(registerRequestDto.password)
        ).also { authRepository.save(it) }
        return generateToken(user)
    }

    private fun generateToken(user: User): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(user.email, user.password)
        SecurityContextHolder.getContext().authentication = authenticationToken;
        val jwt = tokenProvider.createToken(authenticationToken)
        return jwt
    }
}