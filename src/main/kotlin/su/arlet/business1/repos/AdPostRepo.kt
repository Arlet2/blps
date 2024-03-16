package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.AdPost

interface AdPostRepo : JpaRepository<AdPost, Long>