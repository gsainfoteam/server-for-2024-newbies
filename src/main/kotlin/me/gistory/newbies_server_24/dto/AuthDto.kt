package me.gistory.newbies_server_24.dto

data class LoginRequestDto(val email: String, val password: String)
data class RegisterRequestDto(val nickname: String, val email: String, val password: String)
data class TokenResponseDto(val accessToken: String)
