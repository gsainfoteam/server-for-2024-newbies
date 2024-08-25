package me.gistory.newbies_server_24.services

import jakarta.transaction.Transactional
import me.gistory.newbies_server_24.dto.TagDto
import me.gistory.newbies_server_24.entities.Tag
import me.gistory.newbies_server_24.exceptions.TagAlreadyExistException
import me.gistory.newbies_server_24.repositories.TagRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class TagService (
    private val tagRepository: TagRepository
) {
    private val logger = LoggerFactory.getLogger(TagService::class.java)

    fun getTags(): List<TagDto> {
        logger.info("Getting all tags...")
        return tagRepository.findAll().toList().map(Tag::toTagDto)
    }

    fun searchTags(keyword: String): List<TagDto> {
        logger.info("Searching all tags...")
        return tagRepository.findTagsByKeyContaining(keyword).toList().map(Tag::toTagDto)
    }

    fun createTag(tagName: String): Tag {
        logger.info("Creating new tag...")
        tagRepository.findByIdOrNull(tagName)?.let {
            logger.info("Tag already exists.")
            throw TagAlreadyExistException()
        }
        logger.info("Creating new tag...")
        val tag = Tag(
            key = tagName,
        ).also { tagRepository.save(it) }
        return tag
    }
}