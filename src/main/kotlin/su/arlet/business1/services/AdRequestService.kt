package su.arlet.business1.services

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import su.arlet.business1.core.AdRequest
import su.arlet.business1.core.Auditory
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.repos.AdRequestRepo
import su.arlet.business1.repos.UserRepo
import java.time.LocalDate
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class AdRequestService @Autowired constructor(
    private val adRequestRepo: AdRequestRepo,
    private val userRepo: UserRepo,
) {
    @Throws(EntityNotFoundException::class)
    fun createAdRequest(createAdRequest: CreateAdRequest): Long {
        val owner = userRepo.findById(createAdRequest.ownerId).getOrElse {
            throw EntityNotFoundException()
        }

        val adRequest = AdRequest(
            owner = owner,
            auditory = Auditory(
                createAdRequest.ageSegments,
                createAdRequest.incomeSegments,
                createAdRequest.locations,
                createAdRequest.interests
            ),
            requestText = createAdRequest.requestText,
            publishDeadline = createAdRequest.publishDeadline,
            lifeHours = createAdRequest.lifeHours,
            status = AdRequestStatus.SAVED
        )

        val adRequestId = adRequestRepo.save(adRequest).id

        return adRequestId
    }

    @Throws(EntityNotFoundException::class)
    fun updateAdRequest(adRequestId: Long, updateAdRequest: UpdateAdRequest) {
        val adRequest = adRequestRepo.findById(adRequestId).getOrElse {
            throw EntityNotFoundException()
        }

        updateAdRequestFields(adRequest, updateAdRequest)

        adRequestRepo.save(adRequest)
    }

    private fun updateAdRequestFields(adRequest: AdRequest, updateAdRequest: UpdateAdRequest) {
        updateAdRequest.requestText?.let { adRequest.requestText = it }
        updateAdRequest.ageSegments?.let { adRequest.auditory.ageSegments = it }
        updateAdRequest.incomeSegments?.let { adRequest.auditory.incomeSegments = it }
        updateAdRequest.locations?.let { adRequest.auditory.locations = it }
        updateAdRequest.interests?.let { adRequest.auditory.interests = it }
        updateAdRequest.publishDeadline?.let { adRequest.publishDeadline = it }
        updateAdRequest.lifeHours?.let { adRequest.lifeHours = it }
        updateAdRequest.clarificationText?.let { adRequest.clarificationText = it }
    }

    @Throws(EntityNotFoundException::class, IllegalArgumentException::class, UnsupportedOperationException::class)
    fun updateAdRequestStatus(adRequestId: Long, updateStatus: UpdateAdRequestStatus) {
        val adRequest = adRequestRepo.findById(adRequestId).getOrElse {
            throw EntityNotFoundException()
        }
        val newStatus = AdRequestStatus.valueOf(updateStatus.status)

        if (adRequest.status == newStatus) return

        when (newStatus) {
            AdRequestStatus.SAVED ->
                if (adRequest.status != AdRequestStatus.NEEDS_CLARIFICATION)
                    throw UnsupportedOperationException()

            AdRequestStatus.READY_TO_CHECK ->
                if (adRequest.status != AdRequestStatus.SAVED)
                    throw UnsupportedOperationException()

            AdRequestStatus.NEEDS_CLARIFICATION ->
                if (adRequest.status != AdRequestStatus.READY_TO_CHECK)
                    throw UnsupportedOperationException()

            AdRequestStatus.MODERATION ->
                if (adRequest.status != AdRequestStatus.READY_TO_CHECK)
                    throw UnsupportedOperationException()

            AdRequestStatus.READY_TO_PUBLISH ->
                if (adRequest.status != AdRequestStatus.MODERATION)
                    throw UnsupportedOperationException()

            AdRequestStatus.PUBLISHED ->
                if (adRequest.status != AdRequestStatus.READY_TO_PUBLISH)
                    throw UnsupportedOperationException()

            AdRequestStatus.CANCELED ->
                if (adRequest.status == AdRequestStatus.PUBLISHED)
                    throw UnsupportedOperationException()
        }

        adRequest.status = newStatus

        adRequestRepo.save(adRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteAdRequest(adRequestId: Long) {
        if (adRequestRepo.findById(adRequestId).isPresent)
            adRequestRepo.deleteById(adRequestId)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getAdRequest(adRequestId: Long): AdRequest {
        return adRequestRepo.findById(adRequestId).getOrElse {
            throw EntityNotFoundException()
        }
    }

    fun getAdRequests(ownerId: Long?, status: String?): List<AdRequest> {
        val adRequestStatus = try {
            status?.let {
                AdRequestStatus.valueOf(it.uppercase(Locale.getDefault()))
            }
        } catch (_: IllegalArgumentException) { null }

        if (adRequestStatus != null && ownerId != null)
            return adRequestRepo.findAllByOwnerIdAndStatus(ownerId, adRequestStatus)
        if (adRequestStatus != null)
            return adRequestRepo.findAllByStatus(adRequestStatus)
        if (ownerId != null)
            return adRequestRepo.findAllByOwnerId(ownerId)

        return adRequestRepo.findAll()
    }

    data class CreateAdRequest(
        val ownerId: Long,
        @NotBlank var requestText: String,
        val ageSegments: String?,
        val incomeSegments: String?,
        val locations: String?,
        val interests: String?,
        var publishDeadline: LocalDate?,
        @Min(1) var lifeHours: Int?,
    )

    data class UpdateAdRequest(
        val requestText: String?,
        val ageSegments: String?,
        val incomeSegments: String?,
        val locations: String?,
        val interests: String?,
        val publishDeadline: LocalDate?,
        @Min(1) val lifeHours: Int?,
        val clarificationText: String?,
    )

    data class UpdateAdRequestStatus(
        @NotEmpty val status: String,
    )
}