plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.5.1" // mirai-console version
}

mirai {
    coreVersion = "2.5.1" // mirai-core version
}

group = "cn.blrabbit"
version = "1.1.1-dev"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}
