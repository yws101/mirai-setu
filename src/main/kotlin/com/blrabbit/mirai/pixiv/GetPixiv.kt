package com.blrabbit.mirai.pixiv


import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Pixivdata(
    val illusts:List<Illust>,
    val next_url:String,
    val search_span_limit:Int
)
@Serializable
data class Illust(
    val id:Int,
    val title:String,
    val type:String,
    val image_urls:image_url,
    val caption:String?=null,
    val restrict:Int,
    val user:PixivUser,
    val tags: List<Tag>,
    val tools:List<String>?=null,
    val create_date:String,
    val page_count:Int,
    val width:Int,
    val height:Int,
    val sanity_level:Int,
    val x_restrict:Int,
    val series:String? = null,
    val meta_single_page: Meta_single_page? = null,
    val meta_pages:List<Meta_page>? = null,
    val total_view:Int,
    val total_bookmarks:Int,
    val is_bookmarked:Boolean,
    val visible:Boolean,
    val is_muted:Boolean
)
@Serializable
data class image_url(
    val square_medium:String,
    val medium:String,
    val large:String,
    val original:String? = null
)
@Serializable
data class PixivUser(
    val id:Int,
    val name:String,
    val account:String,
    val profile_image_urls:profile_image_url,
    val is_followed:Boolean
)

@Serializable
data class profile_image_url(
    val medium: String
)

@Serializable
data class Tag(
    val name: String,
    val translated_name:String?=null
)

@Serializable
data class Meta_single_page(
    val original_image_url: String? = null
)

@Serializable
data class Meta_page(
    val image_urls:image_url
)

@KtorExperimentalAPI
suspend fun Getillust(word:String): String {
    var msg:String = HttpClient().use { client ->
        client.get("https://api.imjad.cn/pixiv/v2/?type=search&word=$word")
    }
    
    msg = msg.replace("page\":[],","page\":{},")
    val setu: Pixivdata = Json.decodeFromString(msg)
    var mess=""

    setu.illusts[0].let { it ->
        mess += it.title + "\n"
        mess += it.id.toString() + "\n"
        if(it.meta_single_page?.original_image_url == null) {
            it.meta_pages?.forEach {
                mess += it.image_urls.original?.replace("i.pximg.net", "i.pixiv.cat") + "\n"
            }
        }
        else
            mess += it.meta_single_page.original_image_url.replace("i.pximg.net", "i.pixiv.cat") + "\n"
    }

    setu.illusts[1].let { it ->
        mess += it.title + "\n"
        mess += it.id.toString() + "\n"
        if(it.meta_single_page?.original_image_url == null) {
            it.meta_pages?.forEach {
                mess += it.image_urls.original?.replace("i.pximg.net", "i.pixiv.cat") + "\n"
            }
        }
        else
            mess += it.meta_single_page.original_image_url.replace("i.pximg.net", "i.pixiv.cat") + "\n"
    }
    print(mess)

    return mess
}
