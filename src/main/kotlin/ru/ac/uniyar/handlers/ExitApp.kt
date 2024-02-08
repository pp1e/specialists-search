package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.cookie.invalidateCookie

fun exitApp(): HttpHandler = {
    Response(FOUND)
        // .removeCookie(name = "auth_token")
        .headers(headers = listOf(Pair("location", "/home")))
        .invalidateCookie("auth_token")
}
