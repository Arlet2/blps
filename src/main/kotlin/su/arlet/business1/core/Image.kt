package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty


@Entity
@Table(name = "images")
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotEmpty
    var alias: String,

    @NotEmpty
    var link: String,
)