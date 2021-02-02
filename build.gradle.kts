plugins {
    val kotlinVersion = "1.4.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.2.1" // mirai-console version
}

mirai {
    coreVersion = "2.2.1" // mirai-core version
}

group = "com.blrabbit"
version = "0.2.3-dev"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}