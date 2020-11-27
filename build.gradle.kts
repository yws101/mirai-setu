plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version "1.4.10"
    id("net.mamoe.mirai-console") version "1.0.0"
}

group = "com.blrabbit.mirai"
version = "0.2.1"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies{
    //json解析用(明明mirai自带的开发环境却不让用被迫加上，打包的时候还得去掉)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    //sql
    //implementation("org.xerial:sqlite-jdbc:3.32.3.2")
}

mirai{
    excludeDependency("org.jetbrains.kotlinx","kotlinx-serialization-json")
}
