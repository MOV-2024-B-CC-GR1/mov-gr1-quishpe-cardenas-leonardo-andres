plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21" // Agrega este plugin
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Biblioteca de serialización JSON
}

application {
    mainClass.set("MainK\t") // Configura tu clase principal
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(15)
}