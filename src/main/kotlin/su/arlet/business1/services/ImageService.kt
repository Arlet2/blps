package su.arlet.business1.services

import jakarta.validation.constraints.NotEmpty
import org.springframework.stereotype.Service
import su.arlet.business1.core.Image
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.repos.ImageRepo
import kotlin.jvm.optionals.getOrNull

@Service
class ImageService(
    private val imageRepo: ImageRepo,
) {
    fun addImage(createImageRequest: CreateImageRequest): Long {
        val imageId = imageRepo.save(
            Image(
                alias = createImageRequest.alias,
                link = createImageRequest.link,
            )
        ).id

        return imageId
    }

    @Throws(EntityNotFoundException::class)
    fun updateImage(id: Long, updateImageRequest: UpdateImageRequest) {
        val image = imageRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()

        updateImageFields(image, updateImageRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteImage(id: Long) {
        if (imageRepo.findById(id).isPresent)
            imageRepo.deleteById(id)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getImage(id: Long): Image {
        return imageRepo.findById(id).getOrNull() ?: throw EntityNotFoundException()
    }

    fun getImages(link: String?, alias: String?): List<Image> {
        if (link != null && alias != null)
            return imageRepo.findAllByLinkAndAlias(alias, link)
        else if (link != null)
            return imageRepo.findAllByLink(link)
        else if (alias != null)
            return imageRepo.findAllByAlias(alias)

        return imageRepo.findAll()
    }

    data class CreateImageRequest(
        @NotEmpty val alias: String,
        @NotEmpty val link: String,
    )

    data class UpdateImageRequest(
        @NotEmpty val alias: String?,
        @NotEmpty val link: String?,
    )

    private fun updateImageFields(image: Image, updateImageRequest: UpdateImageRequest) {
        updateImageRequest.link?.let { image.link = it }
        updateImageRequest.alias?.let { image.alias = it }
    }

}