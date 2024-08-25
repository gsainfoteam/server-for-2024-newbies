package me.gistory.newbies_server_24.providers

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration}") private val expirationInSeconds: Long = 3600,
) {
    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities.joinToString(",", transform = GrantedAuthority::getAuthority)
        val now = System.currentTimeMillis()
        val expiration = Date(now + expirationInSeconds * 1000)
        return Jwts.builder()
            .subject(authentication.name)
            .claim("AUTH", authorities)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val payload = Jwts.parser().verifyWith(key).build().parse(token).payload as Claims
        val authorities = if (payload["AUTH"] === "") ArrayList() else payload["AUTH"].toString().split(",").map { SimpleGrantedAuthority(it) }
        val user = User(payload.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(user, token, authorities)
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parse(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}