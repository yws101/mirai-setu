plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "6.1.0"
    //id("net.mamoe.mirai-console") version "1.0-RC-dev-32"

}

group = "com.blrabbit.mirai"
version = "0.1.0"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

val miraiCoreVersion = "1.3.2"
val miraiConsoleVersion = "1.0-M4"

dependencies{
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("net.mamoe:mirai-core:$miraiCoreVersion")
    compileOnly("net.mamoe:mirai-console:$miraiConsoleVersion")
    compileOnly("net.mamoe:mirai-core-qqandroid:$miraiCoreVersion")


    testImplementation("net.mamoe:mirai-core:$miraiCoreVersion")
    testImplementation("net.mamoe:mirai-core-qqandroid:$miraiCoreVersion")
    testImplementation("net.mamoe:mirai-console:$miraiConsoleVersion")
    testImplementation("net.mamoe:mirai-console-terminal:1.0-RC-dev-28")
    //http请求用
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    //json解析用
    implementation ("com.beust:klaxon:5.0.1")
}

tasks{
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

