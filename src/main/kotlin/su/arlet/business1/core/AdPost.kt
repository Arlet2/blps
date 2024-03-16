package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import su.arlet.business1.core.enums.AdPostStatus

@Entity
@Table(name = "ad_posts")
class AdPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotNull
    @NotBlank
    var title: String,
    @NotNull
    @NotBlank
    var body: String,
    @NotNull
    @NotBlank
    var targetLink: String,

    @ManyToOne
    var image: Image?,

    @OneToOne
    val adRequest: AdRequest,

    @Enumerated(EnumType.STRING)
    var status: AdPostStatus,
)