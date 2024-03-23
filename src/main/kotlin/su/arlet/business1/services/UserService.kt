package su.arlet.business1.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.exceptions.UserAlreadyExistsException
import su.arlet.business1.exceptions.UserNotFoundException
import su.arlet.business1.exceptions.ValidationException
import su.arlet.business1.repos.UserRepo
import su.arlet.business1.security.jwt.JwtUtils
import kotlin.jvm.optionals.getOrElse


@Service
class UserService @Autowired constructor(
    private val userRepo: UserRepo,
    private val encoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val authenticationManager: AuthenticationManager
) {
    data class AuthorizedUserCredentials(
        val username: String,
        val token: String
    )

    @Throws(ValidationException::class)
    fun login(authUserRequest: AuthUserRequest): AuthorizedUserCredentials {
        val authentication =
            try{
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(authUserRequest.username, authUserRequest.password)
                )
            } catch (e: AuthenticationException) {
                throw UserNotFoundException()
            }
        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtils.generateJwtToken(authentication)

        return AuthorizedUserCredentials(authUserRequest.username!!, jwtToken)
    }

    @Throws(UserAlreadyExistsException::class, ValidationException::class)
    fun register(authUserRequest: AuthUserRequest): AuthorizedUserCredentials {
        if (authUserRequest.username == null || userRepo.findByUsername(authUserRequest.username).isPresent)
            throw UserAlreadyExistsException()
        // TODO validate username and password len

        val user = User(
            name = authUserRequest.name,
            username = authUserRequest.username,
            passwordHash = hashPassword(authUserRequest.password),
            role = UserRole.DEFAULT
        )

        userRepo.save(user)

        return login(authUserRequest)
    }

    @Throws(EntityNotFoundException::class)
    fun updateUser(userId: Long, updateUserRequest: UpdateUserRequest) {
        val user = userRepo.findById(userId).getOrElse {
            throw EntityNotFoundException()
        }

        updateUserFields(user, updateUserRequest)

        userRepo.save(user)
    }

    private fun updateUserFields(user: User, updateUserRequest: UpdateUserRequest) {
        updateUserRequest.name?.let { user.name = it }
        updateUserRequest.password?.let { user.passwordHash = hashPassword(it) }
    }

    @Throws(EntityNotFoundException::class)
    fun deleteUser(userId: Long) {
        if (userRepo.findById(userId).isPresent)
            userRepo.deleteById(userId)
        else
            throw EntityNotFoundException()
    }

    @Throws(EntityNotFoundException::class)
    fun getUser(userId: Long): User {
        return userRepo.findById(userId).getOrElse {
            throw EntityNotFoundException()
        }
    }

    fun getUsers(): List<User> {
        return userRepo.findAll()
    }

    fun hashPassword(password: String?): String {
        if (password == null)
            throw ValidationException("password must be provided")

        return encoder.encode(password)
    }

    data class AuthUserRequest(
        val name: String?,
        val username: String?,
        var password: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (username == null)
                throw ValidationException("login must be provided")
            if (password == null)
                throw ValidationException("password must be provided")
            if (username == "")
                throw ValidationException("login must be not empty")
            if (password == "")
                throw ValidationException("password must be not empty")
        }
    }

    data class UpdateUserRequest(
        val name: String?,
        var password: String?,
    ) {
        @Throws(ValidationException::class)
        fun validate() {
            if (password != null && password == "")
                throw ValidationException("password must be not empty")
        }
    }
}