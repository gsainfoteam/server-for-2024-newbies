package me.gistory.newbies_server_24.controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import me.gistory.newbies_server_24.dto.TagDto
import me.gistory.newbies_server_24.dto.TagListDto
import me.gistory.newbies_server_24.services.TagService
import org.springframework.web.bind.annotation.*

@Tag(name = "Tag")
@RestController
@RequestMapping("/tag")
class TagController (
    private val tagService: TagService,
) {
    @GetMapping("")
    fun getTags(): TagListDto {
        val tags = tagService.getTags()
        return TagListDto(
            count = tags.size,
            list = tags
        )
    }

    @GetMapping("search")
    fun searchTags(
        @RequestParam("keyword") keyword: String,
    ): TagListDto {
        val tags = tagService.searchTags(keyword)
        return TagListDto(
            count = tags.size,
            list = tags
        )
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @PostMapping("")
    fun createTag(@RequestBody tag: TagDto): TagDto {
        return tagService.createTag(tag.key).toTagDto()
    }
}