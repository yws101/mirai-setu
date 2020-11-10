plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion

    id("net.mamoe.mirai-console") version "1.0-RC-1"

}

group = "com.blrabbit.mirai"
version = "0.1.2"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies{

    //http请求用
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    //json解析用
    implementation ("com.beust:klaxon:5.0.1")
    //sql
    //implementation("org.xerial:sqlite-jdbc:3.32.3.2")
}

mirai{

}
