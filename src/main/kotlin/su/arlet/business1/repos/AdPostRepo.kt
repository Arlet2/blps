package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.AdPost
import su.arlet.business1.core.Article
import su.arlet.business1.core.enums.AdPostStatus
import su.arlet.business1.core.enums.ArticleStatus

interface AdPostRepo : JpaRepository<AdPost, Long> {
    fun findAllByStatus(status: AdPostStatus): List<AdPost>
}