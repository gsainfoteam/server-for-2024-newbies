package me.gistory.newbies_server_24.entities

import jakarta.persistence.*
import me.gistory.newbies_server_24.dto.PostDto
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
@Table(name = "posts")
class Post(
    @Column() var title: String,
    @Column() var body: String,

    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: User,

    @ManyToMany
    @JoinTable(name = "post_to_tag")
    var tags: MutableSet<Tag>,

    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
) {
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    @CreationTimestamp
    val createdAt: Date = Date()

    fun toPostDto(): PostDto {
        return PostDto(
            id = id,
            title = title,
            body = body,
            tags = tags.toList().map { it.key },
            board = board.toSummaryDto(),
            createdAt = createdAt,
            createdBy = createdBy.toDto()
        )
    }
}