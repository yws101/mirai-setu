package org.example.mirai.plugin.LoliconV2

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.ruabbit.mirai.setu.Loliconv2Response

@ExperimentalSerializationApi
suspend fun main() {
    val str = "少女|萝莉 白丝|黑丝   女仆"
    val astr = str.split(Regex("\\s+"))

    astr.forEach {
        println(it)
    }

    @Serializable
    data class Loliconrequest(
        val r18:Int,
        val num:Int,
        val keyword: String? = null,
        val tag: MutableList<String>? = null
    )

    val body0 = Loliconrequest(1,5)

    println(Json.encodeToString(body0))

    val normalClient = HttpClient(OkHttp)
    val response: HttpResponse = normalClient.post("https://api.lolicon.app/setu/v2"){
        body = TextContent(Json.encodeToString(body0), ContentType.Application.Json)
    }



    val row = response.content.readUTF8Line()

    /*val json = Json.decodeFromString<Loliconv2Response>(row)

    json.data.forEach {
        println(it)
    }*/
}