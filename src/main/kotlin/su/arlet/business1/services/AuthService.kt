package su.arlet.business1.services

import org.springframework.stereotype.Service

@Service
class AuthService {

    fun getPasswordHash(password: String) : String {
        return password.reversed()
    }
}