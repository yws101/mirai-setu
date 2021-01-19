package com.blrabbit.mirai.bilibili

import com.blrabbit.mirai.MiraiSetuMain
import com.blrabbit.mirai.setu.Downsetu
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Group

@Serializable
data class Moe(
    val RawDocsCount: Int,
    val RawDocsSearchTime: Long,
    val ReRankSearchTime: Long,
    val CacheHit: Boolean,
    val trial: Int,
    val docs: List<Doc>,
    val limit: Int,
    val limit_ttl: Int,
    val quota: Int,
    val quota_ttl: Int
)

@Serializable
data class Doc(
    val from: Float,
    val to: Float,
    val anilist_id: Int,
    val at: Float,
    val season: String,
    val anime: String,
    val filename: String,
    val episode: String? = null,
    val tokenthumb: String,
    val similarity: Float,
    val title: String,
    val title_native: String? = null,
    val title_chinese: String? = null,
    val title_english: String? = null,
    val title_romaji: String,
    val mal_id: Int,
    val synonyms: List<String>? = null,
    val synonyms_chinese: List<String>? = null,
    val is_adult: Boolean
)

@KtorExperimentalAPI
suspend fun serchmoe(Url: String, group: Group){
    val msg: String = HttpClient().use { client ->
        client.get("https://trace.moe/api/search?url=$Url")
    }
    MiraiSetuMain.logger.info(msg)
    val moe = Json {
        isLenient = true
    }.decodeFromString<Moe>(msg)
    var string = ""
    moe.docs.forEach {
        string += "番剧名: ${it.title_native}(${it.title_romaji})\n" +
            "中文名: ${it.title_chinese}\n" +
            "相似度: ${it.similarity}\n" +
            "位置: ${it.episode} ${moe.docs[0].at / 60}\n------------------"
    }
    group.sendMessage(
            string
    )
    group.sendImage(Downsetu("https://trace.moe/thumbnail.php?anilist_id=${moe.docs[0].anilist_id}&file=${moe.docs[0].filename}&t=${moe.docs[0].at}&token=${moe.docs[0].tokenthumb}"))

}