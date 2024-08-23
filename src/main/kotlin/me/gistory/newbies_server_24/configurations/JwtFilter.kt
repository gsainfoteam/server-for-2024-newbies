package me.gistory.newbies_server_24.configurations

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import me.gistory.newbies_server_24.providers.TokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtFilter(private val tokenProvider: TokenProvider) : GenericFilterBean() {
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val token = resolveToken(request)
        token?.let {
            tokenProvider.validateToken(it)
            tokenProvider.getAuthentication(it).let { authentication ->
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(req, res)
    }

    private fun resolveToken(req: HttpServletRequest): String? {
        return req.getHeader(AUTHORIZATION_HEADER)?.let { authorization ->
            if (!authorization.startsWith("Bearer ")) return null
            authorization.split(" ").elementAtOrNull(1).let {
                if (it.isNullOrBlank()) null else it
            }
        }
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }
}