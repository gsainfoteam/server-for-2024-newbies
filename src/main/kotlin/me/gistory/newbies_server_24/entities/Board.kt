package me.gistory.newbies_server_24.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(name = "boards")
class Board(
    @Column(nullable = false) val title: String,

    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: User,
) {
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false)
    @CreationTimestamp
    val createdAt: Date = Date()

    @Column(nullable = false)
    @UpdateTimestamp
    val updatedAt: Date = Date()
}