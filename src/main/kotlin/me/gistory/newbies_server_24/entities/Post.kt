package me.gistory.newbies_server_24.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Entity
@Table(name = "posts")
class Post(
    @Column() val title: String,
    @Column() val body: String,

    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: User,

    @ManyToMany
    @JoinTable(name = "post_to_tag")
    val tags: MutableSet<Tag>,

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
}