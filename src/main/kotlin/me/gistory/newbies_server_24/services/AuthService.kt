package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.LoginRequestDto
import me.gistory.newbies_server_24.dto.RefreshRequestDto
import me.gistory.newbies_server_24.dto.RegisterRequestDto
import me.gistory.newbies_server_24.dto.TokenResponseDto
import me.gistory.newbies_server_24.entities.RefreshToken
import me.gistory.newbies_server_24.entities.User
import me.gistory.newbies_server_24.exceptions.ForbiddenException
import me.gistory.newbies_server_24.exceptions.UserAlreadyExistException
import me.gistory.newbies_server_24.exceptions.UserNotFoundException
import me.gistory.newbies_server_24.providers.TokenProvider
import me.gistory.newbies_server_24.repositories.AuthRepository
import me.gistory.newbies_server_24.repositories.RefreshTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Transactional
@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)
    private val expiresIn = 14 * 24 * 60 * 60 * 1000

    fun login(loginRequestDto: LoginRequestDto): TokenResponseDto {
        val user = authRepository.findByEmail(loginRequestDto.email) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(loginRequestDto.password, user.password)) {
            throw UserNotFoundException()
        }
        return TokenResponseDto(
            accessToken = generateToken(user),
            refreshToken = generateRefreshToken(user),
            expiresIn = expiresIn,
        )
    }

    fun register(registerRequestDto: RegisterRequestDto): TokenResponseDto {
        logger.info("register with email: `{}`", registerRequestDto.email)
        authRepository.findByEmail(registerRequestDto.email)?.let { throw UserAlreadyExistException() }
        val user = User(
            email = registerRequestDto.email,
            password = passwordEncoder.encode(registerRequestDto.password),
            nickname = registerRequestDto.nickname,
        ).also { authRepository.save(it) }
        return TokenResponseDto(
            accessToken = generateToken(user),
            refreshToken = generateRefreshToken(user),
            expiresIn = expiresIn,
        )
    }

    fun refresh(refreshRequestDto: RefreshRequestDto): TokenResponseDto {
        logger.info("refreshing")
        val refreshToken = refreshTokenRepository.findByIdOrNull(refreshRequestDto.refreshToken) ?: throw ForbiddenException()
        refreshTokenRepository.deleteById(refreshRequestDto.refreshToken)
        if (Date().toInstant().toEpochMilli() - refreshToken.createdAt.toInstant().toEpochMilli() > expiresIn) {
            return TokenResponseDto(
                accessToken = "",
                refreshToken = "",
                expiresIn = 0,
            )
        }
        return TokenResponseDto(
            accessToken = generateToken(refreshToken.user),
            refreshToken = generateRefreshToken(refreshToken.user),
            expiresIn = expiresIn,
        )
    }

    private fun generateToken(user: User): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(user.email, user.password)
        SecurityContextHolder.getContext().authentication = authenticationToken
        val jwt = tokenProvider.createToken(authenticationToken)
        return jwt
    }

    private fun generateRefreshToken(user: User): String {
        val refreshToken = getRandomString(40)
        refreshTokenRepository.save(RefreshToken(token = refreshToken, user = user))
        return refreshToken
    }

    private fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}