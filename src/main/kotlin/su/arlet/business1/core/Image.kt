package su.arlet.business1.core

import jakarta.persistence.*


@Entity
@Table(name = "users")
class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var alias: String,
    val link: String,
)