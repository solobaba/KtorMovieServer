package com.solobaba

import io.ktor.server.application.*
import com.solobaba.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureDefaultHeaders()
}