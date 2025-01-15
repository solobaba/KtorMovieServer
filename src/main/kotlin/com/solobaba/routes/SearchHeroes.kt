package com.solobaba.routes

import com.solobaba.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchHeroes() {
    val heroRepository: HeroRepository by inject()

    get("/solobaba/heroes/search") {
        val searchName = call.request.queryParameters["name"]

        val apiResponse = heroRepository.searchHeroes(name = searchName)
        call.respond(
            message = apiResponse,
            status = HttpStatusCode.OK
            )
    }
}