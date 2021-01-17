plugins {
    val kotlinVersion = "1.4.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.0-RC" // mirai-console version
}

mirai {
    coreVersion = "2.0-RC" // mirai-core version
}

group = "com.blrabbit"
version = "0.2.3"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}