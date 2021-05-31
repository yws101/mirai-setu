plugins {
    val kotlinVersion = "1.5.0"
    val miraiVersion = "2.6.5"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version miraiVersion // mirai-console version
}

group = "cn.blrabbit"
version = "1.2.0"

dependencies {
     // implementation("com.alibaba:fastjson:1.2.73")
}

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}
