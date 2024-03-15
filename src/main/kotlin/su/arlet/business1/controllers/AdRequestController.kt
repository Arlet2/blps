package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.AdRequest
import su.arlet.business1.services.AdRequestService

@RestController
@RequestMapping("\${api.path}/ad/requests")
@Tag(name = "Ad requests API")
class AdRequestController @Autowired constructor(
    val adRequestService: AdRequestService
) {
    @GetMapping("/")
    @Operation(summary = "Get ad requests by filters")
    @ApiResponse(
        responseCode = "200", description = "OK", content = [
            Content(array = ArraySchema(items = Schema(implementation = AdRequest::class)))
        ]
    )
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getAdRequests(): ResponseEntity<*> {
        return try {
            val adRequests = adRequestService.getAdRequests()
            ResponseEntity(adRequests, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ad request by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found ad request", content = [
            Content(schema = Schema(implementation = AdRequest::class))
        ]
    )
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - ad request not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getAdRequestById(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            val adRequest = adRequestService.getAdRequest(adRequestId = id)
            ResponseEntity(adRequest, HttpStatus.OK)
        } catch (_: NotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a new ad request")
    @ApiResponse(
        responseCode = "201", description = "Created", content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = Long::class)
            )
        ]
    )
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun createAdRequest(@RequestBody createAdRequest: AdRequestService.CreateAdRequest): ResponseEntity<*> {
        return try {
            val adRequestId = adRequestService.createAdRequest(createAdRequest)
            ResponseEntity(adRequestId, HttpStatus.CREATED)
        } catch (_: NotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update ad request info")
    @ApiResponse(responseCode = "200", description = "Success - updated ad request", content = [Content()])
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - ad request not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateAdRequest(
        @PathVariable id: Long,
        @RequestBody updateAdRequest: AdRequestService.UpdateAdRequest
    ): ResponseEntity<*> {
        return try {
            adRequestService.updateAdRequest(adRequestId = id, updateAdRequest = updateAdRequest)
            ResponseEntity.ok(null)
        } catch (_: NotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/{id}/status")
    @Operation(summary = "Update ad request status")
    @ApiResponse(responseCode = "200", description = "Updated", content = [Content()])
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "409", description = "Invalid status change", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateAdRequestStatus(
        @PathVariable id: Long,
        @RequestBody newStatus: AdRequestService.UpdateAdRequestStatus
    ): ResponseEntity<*> {
        return try {
            adRequestService.updateAdRequestStatus(adRequestId = id, updateStatus = newStatus)
            ResponseEntity(null, HttpStatus.OK)
        } catch (_: NotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (_: UnsupportedOperationException) {
            ResponseEntity("Unsupported status change", HttpStatus.CONFLICT)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ad request")
    @ApiResponse(responseCode = "200", description = "Success - deleted ad request")
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteAdRequest(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            adRequestService.deleteAdRequest(adRequestId = id)
            ResponseEntity.ok(null)
        } catch (_: NotFoundException) {
            ResponseEntity("Ad request not found", HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }
}