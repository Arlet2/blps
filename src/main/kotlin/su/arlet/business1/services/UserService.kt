package su.arlet.business1.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import su.arlet.business1.core.User
import su.arlet.business1.core.enums.UserRole
import su.arlet.business1.exceptions.EntityNotFoundException
import su.arlet.business1.repos.UserRepo
import kotlin.jvm.optionals.getOrElse

@Service
class UserService @Autowired constructor(
    private val userRepo: UserRepo,
    private val authService: AuthService
) {
    @Throws(EntityNotFoundException::class)
    fun createUser(createUserRequest: CreateUserRequest) : Long {
        // TODO check login unique

        val user = User(
            name = createUserRequest.name,
            login = createUserRequest.login,
            passwordHash = authService.getPasswordHash(createUserRequest.password),
            role = UserRole.DEFAULT
        )

        val userId = userRepo.save(user).id

        return userId
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
        updateUserRequest.password?.let { user.passwordHash = authService.getPasswordHash(it) }
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

    data class CreateUserRequest (
        val name: String?,
        val login: String,
        var password: String
    )

    data class UpdateUserRequest (
        val name: String?,
        var password: String?
    )
}