@file:Suppress("DEPRECATION")

package com.solobaba

import com.solobaba.models.ApiResponse
import com.solobaba.repository.HeroRepository
import com.solobaba.repository.NEXT_PAGE_KEY
import com.solobaba.repository.PREVIOUS_PAGE_KEY
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    val heroRepository: HeroRepository by inject(HeroRepository::class.java)

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
    fun `access root endpoint, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(expected = HttpStatusCode.OK, actual = response.status())
                assertEquals(expected = "Welcome to MovieServer API", actual = response.content)
            }
        }
    }

    @Test
    fun `access all heroes, query all pages, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            val pages = 1..5
            val heroes = listOf(
                heroRepository.page1,
                heroRepository.page2,
                heroRepository.page3,
                heroRepository.page4,
                heroRepository.page5
            )
            pages.forEach { page ->
                handleRequest(HttpMethod.Get, "/solobaba/heroes?page=$page").apply {
                    println("CURRENT PAGE $page")
                    assertEquals(
                        expected = HttpStatusCode.OK,
                        actual = response.status()
                    )
                    val expected = ApiResponse(
                        success = true,
                        message = "Successful",
                        prevPage = calculatePage(page = page)["prevPage"],
                        nextPage = calculatePage(page = page)["nextPage"],
                        heroes = heroes[page - 1]
                    )
                    val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    println("PREVIOUS PAGE: ${calculatePage(page = page)["prevPage"]}")
                    println("NEXT PAGE: ${calculatePage(page = page)["nextPage"]}")
                    println("HEROES: ${heroes[page - 1]}")
                    assertEquals(
                        expected = expected,
                        actual = actual
                    )
                }
            }
        }
    }

    @Test
    fun `access all heroes endpoint, query non existing page number, assert error`() {
        testApplication {
            val response = client.get("/solobaba/heroes?page=6")
            assertEquals(
                expected = HttpStatusCode.NotFound,
                actual = response.status
            )
            assertEquals(
                expected = "Page Not Found",
                actual = response.bodyAsText()
            )
        }
    }

    @Test
    fun `access all heroes endpoint, query invalid page number, assert error`() {
        testApplication {
            val response = client.get("/solobaba/heroes?page=invalid")
            assertEquals(
                expected = HttpStatusCode.BadRequest,
                actual = response.status
            )
            val expected = ApiResponse(
                success = false,
                message = "Only numbers allowed"
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
            println("EXPECTED: $expected")
            println("ACTUAL: $actual")
            assertEquals(
                expected = expected,
                actual = actual
            )
        }
    }

    @Test
    fun `access search heroes endpoint, query hero name, assert single hero result`() {
        testApplication {
            val response = client.get("/solobaba/heroes/search?name=sas")
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
            assertEquals(
                expected = 1,
                actual = actual
            )
        }
    }

    @Test
    fun `access search heroes endpoint, query hero name, assert multiple hero result`() {
        testApplication {
            val response = client.get("/solobaba/heroes/search?name=sa")
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes.size
            assertEquals(
                expected = 3,
                actual = actual
            )
        }
    }

    @Test
    fun `access search heroes endpoint, query an empty text, assert empty list as a result`() {
        testApplication {
            val response = client.get("/solobaba/heroes/search?name=")
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes
            assertEquals(
                expected = emptyList(),
                actual = actual
            )
        }
    }

    @Test
    fun `access search heroes endpoint, query non existing hero, assert empty list as a result`() {
        testApplication {
            val response = client.get("/solobaba/heroes/search?name=unknown")
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText()).heroes
            assertEquals(
                expected = emptyList(),
                actual = actual
            )
        }
    }

    @Test
    fun `access non existing endpoint, assert not found`() {
        testApplication {
            val response = client.get("/unknown")
            assertEquals(
                expected = HttpStatusCode.NotFound,
                actual = response.status
            )
            assertEquals(
                expected = "Page Not Found",
                actual = response.bodyAsText()
            )
        }
    }

    private fun calculatePage(page: Int): Map<String, Int?> {
        var prevPage: Int? = page
        var nextPage: Int? = page

        if (page in 1..4) {
            nextPage = nextPage?.plus(1)
        }
        if (page in 2..5) {
            prevPage = prevPage?.minus(1)
        }
        if (page == 1) {
            prevPage = null
        }
        if (page == 5) {
            nextPage = null
        }
        return mapOf(
            PREVIOUS_PAGE_KEY to prevPage,
            NEXT_PAGE_KEY to nextPage
        )
    }

    @Test
    fun `access all heroes endpoint, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/solobaba/heroes").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val expected = ApiResponse(
                    success = true,
                    message = "Successful",
                    prevPage = null,
                    nextPage = 2,
                    heroes = heroRepository.page1
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("EXPECTED: $expected")
                println("ACTUAL: $actual")
                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access all heroes endpoint, query second page, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/solobaba/heroes?page=2").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val expected = ApiResponse(
                    success = true,
                    message = "Successful",
                    prevPage = 1,
                    nextPage = 3,
                    heroes = heroRepository.page2
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("EXPECTED: $expected")
                println("ACTUAL: $actual")
                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }
}