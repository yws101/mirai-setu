package com.blrabbit.mirai.pixiv


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.json.Json

class Pixivdata(
    val illusts:List<Illust>
)
class Illust(
    val id:Int,
    val title:String,
    val caption:String,
    val user:PixivUser,
    val tags: List<Tag>,
    //val meta_single_page: Meta_single_page? = null,
    //val meta_pages:List<Meta_page>? = null
)
class PixivUser(
    val id:Int,
    val name:String,
)
class Tag(
    val name: String,
    val translated_name:String = ""
)
class Meta_single_page(
    val original_image_url: String
)
class Meta_page(
    val original:String
)

@KtorExperimentalAPI
suspend fun Getillust(word:String): String {
    val msg:String = HttpClient(CIO).use { client ->
        client.get("https://api.imjad.cn/pixiv/v2/?type=search&word=$word")
    }

    return msg
}

suspend fun main(){
    print(Getillust("可莉"))
}

