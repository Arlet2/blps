package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.AdRequest
import su.arlet.business1.core.Article

interface AdRequestRepo : JpaRepository<AdRequest, Long> {
}