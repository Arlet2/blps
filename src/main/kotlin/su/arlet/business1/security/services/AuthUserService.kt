package su.arlet.business1.security.services

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import su.arlet.business1.security.jwt.AuthTokenFilter

@Service
class AuthUserService(
    private val authTokenFilter: AuthTokenFilter
) {
    fun getUserId(request: HttpServletRequest): Long {
        return getUserDetails(request).id
    }

    fun getUserDetails(request: HttpServletRequest): AuthUsersDetails {
        return authTokenFilter.getUserDetails(request)!!
    }
}