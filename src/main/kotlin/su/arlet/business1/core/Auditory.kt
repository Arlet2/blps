package su.arlet.business1.core

import jakarta.persistence.Embeddable

@Embeddable
class Auditory(
    var ageSegments: String?,
    var incomeSegments: String?,
    var locations: String?,
    var interests: String?
)