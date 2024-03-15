package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.User
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.services.UserService

@RestController
@RequestMapping("\${api.path}/users")
@Tag(name = "Users API")
class UserController @Autowired constructor(
    val userService: UserService
) {
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponse(
        responseCode = "200", description = "Success - found user", content = [
            Content(schema = Schema(implementation = User::class))
        ]
    )
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getUserById(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            val user = userService.getUser(userId = id)
            ResponseEntity(user, HttpStatus.OK)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a new user")
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
    fun createUser(@RequestBody createUserRequest: UserService.CreateUserRequest): ResponseEntity<*> {
        return try {
            val adRequestId = userService.createUser(createUserRequest)
            ResponseEntity(adRequestId, HttpStatus.CREATED)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user info")
    @ApiResponse(responseCode = "200", description = "Success - updated user", content = [Content()])
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody updateUserRequest: UserService.UpdateUserRequest
    ): ResponseEntity<*> {
        return try {
            userService.updateUser(userId = id, updateUserRequest = updateUserRequest)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("Not found", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "Success - deleted user")
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "400", description = "Bad body", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteUser(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            userService.deleteUser(userId = id)
            ResponseEntity.ok(null)
        } catch (_: EntityNotFoundException) {
            ResponseEntity("User not found", HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            ResponseEntity("Bad body: ${e.message}", HttpStatus.BAD_REQUEST)
        }
    }
}