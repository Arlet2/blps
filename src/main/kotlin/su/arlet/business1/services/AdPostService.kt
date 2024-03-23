package su.arlet.business1.services

import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import su.arlet.business1.core.*
import su.arlet.business1.core.enums.AdPostStatus
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.UnsupportedStatusChangeException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.*
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class AdPostService @Autowired constructor(
    private val adPostRepo: AdPostRepo,
    private val adRequestRepo: AdRequestRepo,
    private val imageRepo: ImageRepo,
    private val userRepo: UserRepo,
    private val adMetricsRepo: AdMetricsRepo,
) {
    @Throws(EntityNotFoundException::class, ValidationException::class)
    fun createAdPost(createAdPost: CreateAdPost): Long {
        val adRequest = adRequestRepo.findById(createAdPost.adRequestId).getOrElse {
            throw EntityNotFoundException("Ad Request")
        }
        val image = createAdPost.imageId?.let {
            imageRepo.findById(it).getOrElse { throw EntityNotFoundException("Image") }
        }
        val salesEditor = createAdPost.salesEditorId?.let {
            userRepo.findById(it).getOrElse { throw EntityNotFoundException("Sales Editor") }
        }

        val adPost = AdPost(
            title = createAdPost.title ?: throw ValidationException("title must be provided"),
            body = createAdPost.body ?: throw ValidationException("body must be provided"),
            targetLink = createAdPost.targetLink ?: throw ValidationException("target link must be provided"),
            salesEditor = salesEditor,
            image = image,
            adRequest = adRequest,
            status = AdPostStatus.SAVED
        )

        val adPostId = adPostRepo.save(adPost).id

        return adPostId
    }

    @Throws(EntityNotFoundException::class)
    fun updateAdPost(adPostId: Long, updateAdPost: UpdateAdPost) {
        val adPost = adPostRepo.findById(adPostId).getOrElse {
            throw EntityNotFoundException("Ad Post")
        }

        updateAdPostFields(adPost, updateAdPost)

        adPostRepo.save(adPost)
    }

    private fun updateAdPostFields(adPost: AdPost, updateAdPost: UpdateAdPost) {
        updateAdPost.title?.let { adPost.title = it }
        updateAdPost.body?.let { adPost.body = it }
        updateAdPost.targetLink?.let { adPost.targetLink = it }
        updateAdPost.salesEditorId?.let {
            adPost.salesEditor = userRepo.findById(it).getOrElse {
                throw EntityNotFoundException("Sales Editor")
            }
        }
        updateAdPost.imageId?.let {
            adPost.image = imageRepo.findById(it).getOrElse {
                throw EntityNotFoundException("Image")
            }
        }
    }

    @Throws(EntityNotFoundException::class, ValidationException::class, UnsupportedStatusChangeException::class)
    fun updateAdPostStatus(adPostId: Long, updateStatus: UpdateAdPostStatus) {
        val adPost = adPostRepo.findById(adPostId).getOrElse {
            throw EntityNotFoundException()
        }

        val newStatus = try {
            AdPostStatus.valueOf(updateStatus.status.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            throw ValidationException("unknown status")
        }

        if (adPost.status == newStatus) return

        when (newStatus) {
            AdPostStatus.SAVED ->
                throw UnsupportedStatusChangeException()

            AdPostStatus.READY_TO_PUBLISH ->
                if (adPost.status != AdPostStatus.SAVED)
                    throw UnsupportedStatusChangeException()

            AdPostStatus.PUBLISHED ->
                if (adPost.status != AdPostStatus.READY_TO_PUBLISH)
                    throw UnsupportedStatusChangeException()

            AdPostStatus.EXPIRED -> {}
        }

        adPost.status = newStatus

        adPostRepo.save(adPost)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteAdPost(adPostId: Long) {
        if (adPostRepo.findById(adPostId).isPresent)
            adPostRepo.deleteById(adPostId)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    @Transactional
    fun getAdPost(adPostId: Long): AdPost {
        val ad = adPostRepo.findById(adPostId).getOrElse {
            throw EntityNotFoundException()
        }

        incViewMetrics(ad)

        return ad
    }

    fun getAdPosts(status: String?): List<AdPost> {
        if (status != null)
            try {
                val adPostStatus = AdPostStatus.valueOf(status.uppercase(Locale.getDefault()))

                return adPostRepo.findAllByStatus(adPostStatus)
            } catch (_: IllegalArgumentException) {
            }

        return adPostRepo.findAll()
    }

    @Transactional(Transactional.TxType.REQUIRED)
    fun incViewMetrics(adPost: AdPost) {
        val metrics = adPost.metrics ?: AdMetrics()
        metrics.viewCounter++

        adPost.metrics = adMetricsRepo.save(metrics)
        adPostRepo.save(adPost)
    }

    data class CreateAdPost(
        @NotBlank var title: String?,
        @NotBlank var body: String?,
        @NotBlank var targetLink: String?,
        var salesEditorId: Long?,
        var imageId: Long?,
        val adRequestId: Long,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (title == null)
                throw ValidationException("title must be provided")
            if (body == null)
                throw ValidationException("body must be provided")
            if (targetLink == null)
                throw ValidationException("target link must be provided")
            if (title == "")
                throw ValidationException("title must be not empty")
            if (body == "")
                throw ValidationException("body must be not empty")
            if (targetLink == "")
                throw ValidationException("target link must be not empty")
        }
    }

    data class UpdateAdPost(
        var title: String?,
        var body: String?,
        var targetLink: String?,
        var salesEditorId: Long?,
        var imageId: Long?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (title != null && title == "")
                throw ValidationException("title must be not empty")
            if (body != null && body == "")
                throw ValidationException("body must be not empty")
            if (targetLink != null && targetLink == "")
                throw ValidationException("target link must be not empty")
        }
    }

    data class UpdateAdPostStatus(
        val status: String,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (status != "")
                throw ValidationException("status must be not empty")
        }
    }
}