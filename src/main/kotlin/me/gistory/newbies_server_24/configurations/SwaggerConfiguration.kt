package me.gistory.newbies_server_24.configurations

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfiguration {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(Components())
        .info(apiInfo())
        .schemaRequirement(
            "bearer",
            SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
        )

    private fun apiInfo() = Info()
        .title("Newbies API")
        .description("24년도 FE 테스트를 위한 API")
        .version("1.0.0")
}
