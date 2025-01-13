package com.solobaba.plugins

import com.solobaba.routes.getAllHeroes
import com.solobaba.routes.root
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
        getAllHeroes()
        static("/images") {
            resources("images")
        }

//        get("/") {
//            call.respondText("Hello World!")
//        }
    }
}
