package me.gistory.newbies_server_24.entities

import jakarta.persistence.*
import me.gistory.newbies_server_24.dto.ImageDto
import java.util.*

@Entity
@Table(name = "image")
class Image (
    @Column(columnDefinition = "TEXT") val image: String,

    @ManyToOne
    @JoinColumn(name = "post_id")
    val post: Post

) {
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID()

    fun toImageDto(): ImageDto {
        return ImageDto(
            image = image,
            id = id
        )
    }
}