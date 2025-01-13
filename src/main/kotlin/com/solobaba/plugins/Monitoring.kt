package com.solobaba.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*

fun Application.configureMonitoring() {
    install(CallLogging)
//    install(CallLogging) {
//        level = Level.INFO
//        filter { call -> call.request.path().startsWith("/") }
//    }
}
