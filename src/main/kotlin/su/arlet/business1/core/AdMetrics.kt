package su.arlet.business1.core

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "ad_metrics")
class AdMetrics(
    @Id
    var id: Long = 0,
    var viewCounter: Int = 0,
)