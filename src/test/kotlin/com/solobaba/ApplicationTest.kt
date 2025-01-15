package com.solobaba

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

//    @Test
//    fun testRoot() = testApplication {
//        application {
//            module()
//        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//        }
//    }

    @Test
    fun testRoot() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(expected = HttpStatusCode.OK, actual = response.status())
                assertEquals(expected = "Welcome to MovieServer API", actual = response.content)
            }
        }
    }
}