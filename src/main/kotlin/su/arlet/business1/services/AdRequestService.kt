package su.arlet.business1.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import su.arlet.business1.core.AdRequest
import su.arlet.business1.core.Auditory
import su.arlet.business1.core.enums.AdRequestStatus
import su.arlet.business1.repos.AdRequestRepo
import su.arlet.business1.repos.UserRepo
import java.time.LocalDate
import kotlin.jvm.optionals.getOrElse

@Service
class AdRequestService @Autowired constructor(
    private val adRequestRepo: AdRequestRepo,
    private val userRepo: UserRepo
) {
    @Throws(NotFoundException::class)
    fun createAdRequest(createAdRequest: CreateAdRequest) : Long {
        val owner = userRepo.findById(createAdRequest.ownerId).getOrElse {
            throw NotFoundException()
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
            status = AdRequestStatus.READY_TO_CHECK
        )

        val adRequestId = adRequestRepo.save(adRequest).id

        return adRequestId
    }

    @Throws(NotFoundException::class)
    fun updateAdRequest(adRequestId: Long, updateAdRequest: UpdateAdRequest) {
        val adRequest = adRequestRepo.findById(adRequestId).getOrElse {
            throw NotFoundException()
        }

        updateAdRequestFields(adRequest, updateAdRequest)

        adRequestRepo.save(adRequest)
    }

    private fun updateAdRequestFields(adRequest: AdRequest, updateAdRequest: UpdateAdRequest) {
        updateAdRequest.requestText?.let { adRequest.requestText = it }
        updateAdRequest.ageSegments?.let { adRequest.auditory.ageSegments = it }
        updateAdRequest.incomeSegments?.let { adRequest.auditory.incomeSegments = it}
        updateAdRequest.locations?.let { adRequest.auditory.locations = it }
        updateAdRequest.interests?.let { adRequest.auditory.interests = it }
        updateAdRequest.publishDeadline?.let { adRequest.publishDeadline = it }
        updateAdRequest.lifeHours?.let { adRequest.lifeHours = it }
    }

    @Throws(NotFoundException::class)
    fun deleteAdRequest(adRequestId: Long) {
        if (adRequestRepo.findById(adRequestId).isPresent)
            adRequestRepo.deleteById(adRequestId)
        else
            throw NotFoundException()
    }

    @Throws(NotFoundException::class)
    fun getAdRequest(adRequestId: Long): AdRequest {
        return adRequestRepo.findById(adRequestId).getOrElse {
            throw NotFoundException()
        }
    }

    fun getAdRequests(): List<AdRequest> {
        return adRequestRepo.findAll()
    }

    data class CreateAdRequest (
        val ownerId: Long,
        var requestText: String,
        val ageSegments: String?,
        val incomeSegments: String?,
        val locations: String?,
        val interests: String?,
        var publishDeadline: LocalDate?,
        var lifeHours: Int?,
    )

    data class UpdateAdRequest (
        val requestText: String?,
        val ageSegments: String?,
        val incomeSegments: String?,
        val locations: String?,
        val interests: String?,
        val publishDeadline: LocalDate?,
        val lifeHours: Int?,
    )
}