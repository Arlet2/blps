package su.arlet.business1.core

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "article_metrics")
class ArticleMetrics(
    @Id
    var id: Long = 0,
    var viewCounter: Int = 0,
    var readCounter: Int = 0,
)