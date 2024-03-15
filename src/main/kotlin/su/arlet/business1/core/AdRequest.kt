package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import su.arlet.business1.core.enums.AdRequestStatus
import java.time.LocalDate

@Entity
@Table(name = "ad_requests")
class AdRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotNull
    @ManyToOne
    val owner: User,
    @NotNull
    @Embedded
    var auditory: Auditory,

    @NotNull
    @NotEmpty
    var requestText: String,
    var clarificationText: String? = null,
    var publishDeadline: LocalDate?,
    @Min(1)
    var lifeHours: Int?,

    @NotNull
    @Enumerated(EnumType.STRING)
    var status: AdRequestStatus
)