package su.arlet.business1.services

import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import su.arlet.business1.core.AdPost
import su.arlet.business1.core.Article
import su.arlet.business1.core.Image
import su.arlet.business1.core.enums.ArticleStatus
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.repos.AdPostRepo
import su.arlet.business1.repos.ArticleRepo
import su.arlet.business1.repos.ImageRepo
import su.arlet.business1.repos.UserRepo
import kotlin.jvm.optionals.getOrNull

@Service
class ArticleService(
    private val articleRepo: ArticleRepo,
    private val adPostRepo: AdPostRepo,
    private val imageRepo: ImageRepo,
    private val userRepo: UserRepo,
) {
    fun addArticle(createArticleRequest: CreateArticleRequest): Long {
        val author = userRepo.findById(createArticleRequest.authorId).getOrNull() ?: throw EntityNotFoundException()

        val articleId = articleRepo.save(
            Article(
                title = createArticleRequest.title,
                text = createArticleRequest.text,
                images = getImagesById(createArticleRequest.imageIds),
                status = ArticleStatus.ON_REVIEW,
                author = author,
            )
        ).id

        return articleId
    }

    private fun getImagesById(ids: List<Long>): List<Image> {
        val images = mutableListOf<Image>()

        for (id in ids) {
            val image = imageRepo.findById(id).getOrNull()
            if (image == null) {
                println("warning: image with id=${id} is not found. It will be ignored on creating article")
                continue
            }

            images.add(image)
        }

        return images
    }

    @Throws(EntityNotFoundException::class)
    fun updateArticle(id: Long, updateArticleRequest: UpdateArticleRequest) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        updateArticleFields(article, updateArticleRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteArticle(id: Long) {
        if (articleRepo.findById(id).isPresent)
            articleRepo.deleteById(id)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getArticle(id: Long): Article {
        return articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()
    }

    fun getArticles(status: ArticleStatus?): List<Article> {
        if (status != null) {
            return articleRepo.findAllByStatus(status)
        }
        return articleRepo.findAll()
    }

    @Throws(EntityNotFoundException::class, UnsupportedOperationException::class)
    fun updateArticleStatus(id: Long, newStatus: ArticleStatus, initiatorId: Long) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        when (newStatus) {
            ArticleStatus.ON_REVIEW -> {
                if (article.status != ArticleStatus.NEED_FIXES)
                    throw UnsupportedOperationException()
            }

            ArticleStatus.NEED_FIXES -> {
                if (article.status != ArticleStatus.ON_REVIEW)
                    throw UnsupportedOperationException()
            }

            ArticleStatus.PUBLISHED -> {
                if (article.status != ArticleStatus.APPROVED)
                    throw UnsupportedOperationException()
            }

            ArticleStatus.APPROVED -> {
                if (article.status != ArticleStatus.ON_REVIEW)
                    throw UnsupportedOperationException()

                val editor = userRepo.findById(initiatorId).getOrNull() ?: throw EntityNotFoundException()

                article.editor = editor
            }
        }

        article.status = newStatus

        articleRepo.save(article)
    }

    @Throws(EntityNotFoundException::class)
    fun updateArticleAds(id: Long, newAdPostIds: List<Long>) {
        val article = articleRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        val newAds = mutableListOf<AdPost>()

        for (adPostId in newAdPostIds) {
            val adPost = adPostRepo.findById(adPostId).getOrNull()
            if (adPost == null) {
                println("warning: ad post with id=${adPostId} is not found. It will be ignored on updating article")
                continue
            }

            newAds.add(adPost)
        }

        article.adPosts = newAds

        articleRepo.save(article)
    }

    data class CreateArticleRequest(
        val id: Long,
        @NotBlank val title: String,
        @NotBlank val text: String,
        val imageIds: List<Long>,
        val authorId: Long,
    )

    data class UpdateArticleRequest(
        val title: String?,
        val text: String?,
        val imageIds: List<Long>?,
        val clarificationText: String?,
    )

    private fun updateArticleFields(article: Article, updateArticleRequest: UpdateArticleRequest) {
        updateArticleRequest.text?.let { article.text = it }
        updateArticleRequest.title?.let { article.title = it }
        updateArticleRequest.imageIds?.let {
            val newImages = mutableListOf<Image>()

            for (imageId in updateArticleRequest.imageIds) {
                val image = imageRepo.findById(imageId).getOrNull()
                if (image == null) {
                    println("warning: image with id=${imageId} is not found. It will be ignored on updating article")
                    continue
                }

                newImages.add(image)
            }

            article.images = newImages
        }
        updateArticleRequest.clarificationText?.let { article.clarificationText = it }
    }
}