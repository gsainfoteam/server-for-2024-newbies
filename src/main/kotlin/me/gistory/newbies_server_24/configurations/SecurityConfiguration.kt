package me.gistory.newbies_server_24.configurations

import me.gistory.newbies_server_24.providers.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatchers
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
class SecurityConfiguration {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity, tokenProvider: TokenProvider): SecurityFilterChain {
        httpSecurity.invoke {
            csrf { disable() }
            cors {
                configurationSource = CorsConfigurationSource { CorsConfiguration().applyPermitDefaultValues() }
            }
            headers {
                frameOptions { sameOrigin = true }
            }
            formLogin { disable() }
            httpBasic { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeHttpRequests {
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/api/**", permitAll)
                authorize("/error", permitAll)
                authorize(
                    RequestMatchers.anyOf(
                        AntPathRequestMatcher.antMatcher("/auth/**"),
                        AntPathRequestMatcher.antMatcher("/"),
                    ), permitAll
                )
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtFilter(tokenProvider))
        }
        return httpSecurity.build()
    }
}