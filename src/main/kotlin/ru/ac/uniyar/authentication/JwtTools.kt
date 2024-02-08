package ru.ac.uniyar.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.LocalDateTime
import java.time.ZoneOffset

class JwtTools(
    private val salt: String,
    private val organization: String,
    private val lifespanDays: Long,
) {
    fun createToken(userID: String): String {
        val date = LocalDateTime.now().plusDays(lifespanDays).toInstant(ZoneOffset.UTC)
        return JWT.create()
            .withSubject(userID)
            .withExpiresAt(date)
            .withIssuer(organization)
            .sign(Algorithm.HMAC512(salt))
    }

    fun checkToken(token: String): String = JWT.require(Algorithm.HMAC512(salt))
        .build()
        .verify(token)
        .subject

    fun printLifespan(token: String) {
        println(
            JWT.require(
                Algorithm.HMAC512(salt)
            )
                .build()
                .verify(token).expiresAt
        )
    }
}
