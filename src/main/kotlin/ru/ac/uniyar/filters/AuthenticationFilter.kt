package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.authentication.JwtTools
import ru.ac.uniyar.authentication.User

class AuthenticationFilter(val key: BiDiLens<Request, User?>, val getUser: (String) -> User?, jwtTools: JwtTools) {
    val authenticationFilter = Filter { next: HttpHandler ->
        { request: Request ->
            val tokenCookie = request.cookie("auth_token")
            val newRequest = if (tokenCookie != null) {
                val user = getUser(jwtTools.checkToken(tokenCookie.value))
                if (user != null)
                    request.with(key of user)
                else
                    request
            } else
                request

            next(newRequest)
        }
    }
}
