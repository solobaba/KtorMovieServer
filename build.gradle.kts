val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"

    //alias(libs.plugins.kotlin.jvm)
    //alias(libs.plugins.ktor)
    //alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.solobaba"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    //val isDevelopment: Boolean = project.ext.has("development")
    //applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")

    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

//    implementation(libs.ktor.server.content.negotiation)
//    implementation(libs.ktor.client.content.negotiation)
//    implementation(libs.ktor.server.core)
//    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation(libs.ktor.server.call.logging)
//    implementation(libs.ktor.server.netty)
//    implementation(libs.logback.classic)
//    implementation(libs.ktor.server.status.pages)
//
//    //Koin for Ktor
//    implementation("io.insert-koin:koin-ktor:3.4.0")
//    //SLF4J Logger
//    implementation("io.insert-koin:koin-logger-slf4j:3.4.0")
//
//    //DefaultHeaders
//    implementation("io.ktor:ktor-server-default-headers:3.0.2")
//
//    testImplementation(libs.ktor.server.test.host)
//    testImplementation(libs.kotlin.test.junit)
}
