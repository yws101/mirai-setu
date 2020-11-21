plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion

    id("net.mamoe.mirai-console") version "1.0.0"

}

group = "com.blrabbit.mirai"
version = "0.2.0"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies{
    //json解析用
    implementation ("com.beust:klaxon:5.0.1")
    //sql
    //implementation("org.xerial:sqlite-jdbc:3.32.3.2")
}

mirai{

}
