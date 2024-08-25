package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.CreatePostDto
import me.gistory.newbies_server_24.dto.UpdatePostDto
import me.gistory.newbies_server_24.entities.Post
import me.gistory.newbies_server_24.exceptions.*
import me.gistory.newbies_server_24.repositories.AuthRepository
import me.gistory.newbies_server_24.repositories.BoardRepository
import me.gistory.newbies_server_24.repositories.PostRepository
import me.gistory.newbies_server_24.repositories.TagRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class PostService (
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository,
    private val boardRepository: BoardRepository,
    private val authRepository: AuthRepository,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun getPosts(boardId: UUID?, tagId: String?): List<Post> {
        if (boardId == null && tagId == null) {
            return postRepository.findAll().toList()
        } else if (boardId == null && tagId != null) {
            val tag = tagRepository.findByIdOrNull(tagId) ?: return postRepository.findAll().toList()
            return postRepository.findByTags(tag)
        } else if (tagId == null && boardId != null) {
            val board = boardRepository.findByIdOrNull(boardId) ?: return postRepository.findAll().toList()
            return postRepository.findByBoard(board)
        } else if (tagId != null && boardId != null) {
            val tag = tagRepository.findByIdOrNull(tagId) ?: return postRepository.findAll().toList()
            val board = boardRepository.findByIdOrNull(boardId) ?: return postRepository.findAll().toList()
            return postRepository.findByTagsAndBoard(tag, board)
        }
        throw ForbiddenException()
    }

    fun searchPost(keyword: String): List<Post> {
        return postRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword)
    }

    fun createPost(dto: CreatePostDto, boardId: UUID, userEmail: String): Post {
        val board = boardRepository.findByIdOrNull(boardId) ?: throw BoardNotFoundException()
        val user = authRepository.findByEmail(userEmail) ?: throw UserNotFoundException()
        val tags = try {
            tagRepository.findAllById(dto.tags).toMutableSet()
        } catch(e: Error) {
            throw TagNotFoundException()
        }
        return postRepository.save(Post(
            title = dto.title,
            body = dto.body,
            tags = tags,
            createdBy = user,
            board = board,
        ))
    }

    fun updatePost(dto: UpdatePostDto, postId: UUID, userEmail: String): Post {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        val user = authRepository.findByEmail(userEmail) ?: throw UserNotFoundException()
        if (post.createdBy.id != user.id) {
            throw ForbiddenException()
        }
        post.title = dto.title ?: post.title
        post.body = dto.body ?: post.body
        if (dto.tags != null) {
            val tags = try {
                tagRepository.findAllById(dto.tags).toMutableSet()
            } catch(e: Error) {
                throw TagNotFoundException()
            }
            post.tags = tags
        }
        return postRepository.save(post)
    }

    fun deletePost(postId: UUID, userEmail: String): Unit {
        val user =  authRepository.findByEmail(userEmail) ?: throw UserNotFoundException()
        val post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException()
        if (post.createdBy.id != user.id) {
            throw ForbiddenException()
        }
        postRepository.delete(post)
    }
}