package me.gistory.newbies_server_24.configurations

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "bearer",
    scheme = "bearer",
)
class DocsConfiguration