package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank


@Entity
@Table(name = "images")
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank
    var alias: String,

    @NotBlank
    var link: String,
)