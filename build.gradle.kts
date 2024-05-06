plugins {
    id("io.freefair.lombok") version "8.1.0"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.23"
    id("application")
    id("org.flywaydb.flyway") version "10.12.0"

    kotlin("jvm") version "1.9.23"
    kotlin("plugin.lombok") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "com.tomohavvk.limit"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.arrow-kt:arrow-core:1.2.4")
    implementation("io.arrow-kt:arrow-core-serialization:1.2.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.zeko:zeko-sql-builder:1.4.0")

    implementation("org.postgresql:r2dbc-postgresql:1.0.1.RELEASE")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.flywaydb:flyway-core:9.22.3")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

application {
    mainClass.set("com.tomohavvk.limit.ApplicationKt")
}

kotlinLombok {
    lombokConfigurationFile(file("lombok.config"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}