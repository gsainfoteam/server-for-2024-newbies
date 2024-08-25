package me.gistory.newbies_server_24.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.util.Date

@Entity
@Table(name = "refresh_token")
class RefreshToken (
    @Id
    val token: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
){
    @Column(nullable = false)
    @CreationTimestamp
    val createdAt: Date = Date()
}