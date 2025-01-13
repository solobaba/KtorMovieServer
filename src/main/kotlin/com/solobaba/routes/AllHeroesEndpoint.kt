package com.solobaba.routes

import com.solobaba.models.ApiResponse
import com.solobaba.repository.HeroRepository
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getAllHeroes() {
    val heroRepository: HeroRepository by inject()

    get("/solobaba/heroes") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            require(page in 1..5)

            val apiResponse = heroRepository.getAllHeroes(page = page)
            call.respond(
                message = apiResponse,
                status = HttpStatusCode.OK
            )
            //call.respond(message = page)
        } catch (e: NumberFormatException) {
            call.respond (message = ApiResponse(success = false, message = "Only numbers allowed"),
                status = HttpStatusCode.BadRequest)
        } catch (e: IllegalArgumentException) {
            call.respond (message = ApiResponse(success = false, message = "Heroes not found"),
                status = HttpStatusCode.NotFound)
        }
    }
}