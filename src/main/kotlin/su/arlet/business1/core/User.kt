package su.arlet.business1.core

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.core.enums.UserRole

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String?,
    val login: String,
    var passwordHash: String,

    @NotNull
    @Enumerated(EnumType.STRING)
    val role: UserRole
)