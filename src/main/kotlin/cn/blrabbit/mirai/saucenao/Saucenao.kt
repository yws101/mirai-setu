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
import okhttp3.internal.ignoreIoExceptions
import java.net.URLDecoder
import java.nio.charset.Charset

class Saucenao(private val subject: Contact) {

    data class Suacenaodata(
        var similarity: String, // 相似度
        var thumbnail: String, // 图片获取链接
        var ext_urls: List<String>, // 图片源链接
        var title: String // 图片标题
    )

    var data: MutableList<Suacenaodata> = mutableListOf()

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
                client.get(
                    "https://saucenao.com/search.php?" +
                        "output_type=2&" +
                        "api_key=5fe827fb6ef3284d73a031760cb2f7185ce1b380&" +
                        "db=999&" +
                        "numres=2&" +
                        "url=${URLDecoder.decode(image.queryUrl(), Charset.forName("utf-8"))}"
                )
            parsejson(json)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(MySetting.LoliconAPIKEY, "/$/{APIKEY/}"))
            MiraiSetuMain.logger.error(e)
            throw e
        }
    }

    private fun parsejson(json: String) {
        val result: SaucenaoJson = Json {
            ignoreUnknownKeys = true
        }.decodeFromString(json)
        result.result.forEach {
            data.add(
                Suacenaodata(
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
