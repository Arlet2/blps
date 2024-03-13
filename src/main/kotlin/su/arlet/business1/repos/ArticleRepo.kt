package su.arlet.business1.repos

import org.springframework.data.jpa.repository.JpaRepository
import su.arlet.business1.core.Article

interface ArticleRepo : JpaRepository<Article, Long> {
}