package me.gistory.newbies_server_24.configurations

import me.gistory.newbies_server_24.providers.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity, tokenProvider: TokenProvider): SecurityFilterChain =
        httpSecurity
            .csrf { csrf -> csrf.disable() }
            .cors { it.disable() }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { req ->
                req.requestMatchers(HttpMethod.GET).permitAll()
                req.requestMatchers("/swagger-ui/**" , "v3/api-docs/**", "/api-docs/**").permitAll()
                req.requestMatchers("/auth/login", "/auth/register", "auth/refresh").permitAll()
                req.requestMatchers("/error").permitAll()
                req.requestMatchers("/").permitAll()
                req.anyRequest().authenticated()
            }
            .addFilterBefore(JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            .build()


}