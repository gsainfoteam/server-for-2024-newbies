package me.gistory.newbies_server_24.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Column(unique = true, nullable = false) val email: String,
    @Column(nullable = false) val password: String,
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
