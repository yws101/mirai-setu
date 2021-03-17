package cn.blrabbit.mirai.saucenao

import cn.blrabbit.mirai.MiraiSetuMain
import cn.blrabbit.mirai.saucenao.jsondata.SaucenaoJson
import cn.blrabbit.mirai.utils.storge.MySetting
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl

class Saucenao(val subject: Contact) {

    data class suacenaodata(
        var similarity: String, // 相似度
        var thumbnail: String, // 图片获取链接
        var ext_urls: List<String>, // 图片源链接
        var title: String // 图片标题
    )

    var data: MutableList<suacenaodata> = mutableListOf()

    companion object {
        @KtorExperimentalAPI
        private val client = HttpClient(OkHttp) {
            // 似乎还不是很需要代理，测试联通网速度很快。
            /* engine {
                 proxy = when (MySetting.proxyconfig) {
                     0 -> null
                     1 -> ProxyBuilder.http(MySetting.httpproxy.proxy)
                     2 -> ProxyBuilder.socks(host = MySetting.socksproxy.host, port = MySetting.socksproxy.port)
                     else -> null
                 }*/
        }

        @KtorExperimentalAPI
        fun closeClient() {
            client.close()
        }
    }

    @KtorExperimentalAPI
    suspend fun search(image: Image) {
        try {
            val json: String =
                client.get("https://saucenao.com/search.php?db=999&output_type=2&testmode=1&numres=16&url=http%3A%2F%2Fsaucenao.com%2Fimages%2Fstatic%2Fbanner.gif")
            parsejson(json)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(MySetting.APIKEY, "/$/{APIKEY/}"))
            MiraiSetuMain.logger.error(e)
            throw e
        }
    }

    fun parsejson(json: String) {
        val result: SaucenaoJson = Json.decodeFromString(json)

        result.result.forEach {
            data.add(
                suacenaodata(
                    similarity = it.header.similarity,
                    thumbnail = it.header.thumbnail,
                    ext_urls = it.data.ext_urls,
                    title = it.data.title
                )
            )
        }
    }

    suspend fun sendmessage() {
        subject.sendMessage(data.toString())
    }
}