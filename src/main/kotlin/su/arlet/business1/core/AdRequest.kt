package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import su.arlet.business1.core.enums.AdRequestStatus
import java.time.LocalDate

@Entity
@Table(name = "ad_requests")
class AdRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val owner: User,
    @Embedded
    var auditory: Auditory,

    @NotNull
    var requestText: String,
    var publishDeadline: LocalDate?,
    @Min(1)
    var lifeHours: Int?,

    @NotNull
    @Enumerated(EnumType.STRING)
    val status: AdRequestStatus
)