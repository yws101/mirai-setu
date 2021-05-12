plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.6.4" // mirai-console version
}

group = "cn.blrabbit"
version = "1.5"

dependencies {
    implementation("com.alibaba:fastjson:1.2.73")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}
