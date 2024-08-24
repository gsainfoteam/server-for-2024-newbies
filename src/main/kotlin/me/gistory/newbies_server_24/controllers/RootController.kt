package me.gistory.newbies_server_24.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import me.gistory.newbies_server_24.dto.ApiInfoDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "Api info")
@RestController
class RootController {
    private val publishedAt = Date()

    @GetMapping("/")
    fun getApiInfo(): ApiInfoDto {
        return ApiInfoDto(
            name = "Newbies API",
            version = "1.0.0",
            publishedAt = publishedAt,
        )
    }
}