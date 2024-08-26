package me.gistory.newbies_server_24.controllers

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import me.gistory.newbies_server_24.dto.CreatePostDto
import me.gistory.newbies_server_24.dto.PostDto
import me.gistory.newbies_server_24.dto.PostListDto
import me.gistory.newbies_server_24.dto.UpdatePostDto
import me.gistory.newbies_server_24.entities.Post
import me.gistory.newbies_server_24.services.PostService
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Tag(name = "Post")
@RestController
@RequestMapping("/posts")
class PostController (
    private val postService: PostService
) {

    @GetMapping("")
    fun getPosts(
        @RequestParam(value = "tag", required = false) tag: String?,
        @RequestParam(value = "boardUuid", required = false) boardUuid: String?,
    ): PostListDto {
        val boardId = boardUuid?.let { UUID.fromString(it) }
        val posts = postService.getPosts(boardId = boardId, tagId = tag).toList()
        return PostListDto(
            count = posts.size,
            list = posts.map(Post::toPostDto)
        )
    }

    @GetMapping("/search")
    fun searchPosts(
        @RequestParam(value = "keyword") keyword: String
    ): PostListDto {
        val posts = postService.searchPost(keyword).toList()
        return PostListDto(
            count = posts.size,
            list = posts.map(Post::toPostDto)
        )
    }

    @GetMapping("/{uuid}")
    fun getPost(@PathVariable uuid: String): PostDto {
        return postService.getPost(UUID.fromString(uuid)).toPostDto()
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @PostMapping("")
    fun createPost(
        @RequestBody post: CreatePostDto,
        @RequestParam(value = "boardUuid") boardUuid: String,
        authentication: Authentication
    ): PostDto {
        return postService.createPost(post, UUID.fromString(boardUuid), authentication.name).toPostDto()
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @PostMapping("/{uuid}/image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(
        @PathVariable("uuid") uuid: String,
        @RequestPart("file") file: MultipartFile,
        authentication: Authentication
    ) {
        return postService.uploadImage(UUID.fromString(uuid), file, authentication.name)
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @DeleteMapping("/{uuid}/image/{imageId}")
    fun deleteImage(
        @PathVariable("uuid") uuid: String,
        @PathVariable("imageId") imageId: String,
        authentication: Authentication
    ) {
        return postService.deleteImage(UUID.fromString(uuid), UUID.fromString(imageId), authentication.name )
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @PatchMapping("/{uuid}")
    fun updatePost(
        @RequestBody post: UpdatePostDto,
        @PathVariable("uuid") uuid: String,
        authentication: Authentication
    ): PostDto {
        return postService.updatePost(post, UUID.fromString(uuid), authentication.name).toPostDto()
    }

    @SecurityRequirement(name = "Bearer Authorization")
    @DeleteMapping("/{uuid}")
    fun deletePost(
        @PathVariable("uuid") uuid: String,
        authentication: Authentication
    ): Unit {
        return postService.deletePost(UUID.fromString(uuid), authentication.name)
    }
}