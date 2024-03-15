package su.arlet.business1.core

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class Auditory(
    var ageSegments: String?,
    var incomeSegments: String?,
    var locations: String?,
    var interests: String?
)