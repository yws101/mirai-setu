plugins {
    val kotlinVersion = "1.5.10"
    val miraiVersion = "2.7-M2"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version miraiVersion // mirai-console version
}

group = "moe.ruabbit"
version = "1.2.2"

dependencies {
    // 其实用依赖来实现一些功能代码更简练，但编译出来的jar可就不小了
    // implementation("com.alibaba:fastjson:1.2.73")
    // implementation("io.ktor:ktor-client-serialization:1.5.4")
}

repositories {
    // mavenLocal()
    maven("https://repo.huaweicloud.com/repository/maven/")
    maven("https://maven.aliyun.com/repository/public")
    // mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-eap")
}
