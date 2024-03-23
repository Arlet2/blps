package su.arlet.business1.controllers


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.security.services.AuthUserService
import su.arlet.business1.services.UserService


@RestController
@RequestMapping("\${api.path}/users")
@Tag(name = "Users API")
class UserController(
    val userService: UserService,
    val authUserService: AuthUserService
) {
    data class UserEntity(
        val id: Long,
        val name: String?,
        val username: String,
        val role: UserRole,
    )

    @GetMapping("/")
    @Operation(summary = "Get current user")
    @ApiResponse(
        responseCode = "200", description = "Success - found user", content = [
            Content(schema = Schema(implementation = User::class))
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun getUserById(request: HttpServletRequest): ResponseEntity<*> {
        val userId = authUserService.getUserId(request)
        val user = userService.getUser(userId = userId)

        return ResponseEntity(
            UserEntity(
                id = user.id,
                name = user.name,
                username = user.username,
                role = user.role,
            ),
            HttpStatus.OK,
        )
    }

    @PatchMapping("/")
    @Operation(summary = "Update user info")
    @ApiResponse(responseCode = "200", description = "Success - updated user", content = [Content()])
    @ApiResponse(
        responseCode = "400", description = "Bad body", content = [
            Content(schema = Schema(implementation = String::class)),
        ]
    )
    @ApiResponse(responseCode = "404", description = "Not found - user not found", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun updateUser(
        @RequestBody updateUserRequest: UserService.UpdateUserRequest,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        updateUserRequest.validate()

        val userId = authUserService.getUserId(request)
        userService.updateUser(userId = userId, updateUserRequest = updateUserRequest)

        return ResponseEntity.ok(null)
    }

    @DeleteMapping("/")
    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "200", description = "Success - deleted user", content = [Content()])
    @ApiResponse(responseCode = "204", description = "No content", content = [Content()])
    @ApiResponse(responseCode = "500", description = "Server error", content = [Content()])
    fun deleteUser(request: HttpServletRequest): ResponseEntity<*> {
        val userId = authUserService.getUserId(request)

        userService.deleteUser(userId = userId)

        return ResponseEntity.ok(null)
    }
}