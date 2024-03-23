package su.arlet.business1.security.sevices

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import su.arlet.business1.exceptions.UserNotFoundException
import su.arlet.business1.repos.UserRepo
import su.arlet.business1.security.jwt.JwtUtils

@Service
class AuthUsersDetailsService(
    private val userRepository: UserRepo,
) : UserDetailsService {
    @Transactional
    @Throws(UserNotFoundException::class)
    override fun loadUserByUsername(username: String): AuthUsersDetails {
        val user = userRepository.findByUsername(username)

        if (user.isEmpty) throw UsernameNotFoundException("User Not Found with username: $username")

        return AuthUsersDetails.build(user.get())
    }
}
