package su.arlet.business1.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import su.arlet.business1.core.Article
import su.arlet.business1.repos.ArticleRepo

@Service
class ArticleService @Autowired constructor(
    articleRepo: ArticleRepo
) {
    fun addArticle() {

    }

    fun updateArticle() {

    }

    fun deleteArticle() {

    }

    fun getArticle(): Article {

    }

    fun getArticles(): List<Article> {

    }
}