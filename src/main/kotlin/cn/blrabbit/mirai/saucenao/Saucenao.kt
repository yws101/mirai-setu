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
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.Charset

class Saucenao(private val subject: Contact) {

    var result: SaucenaoJson.Result? = null
/*    private val db: String = "5" //数据库*/

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
                        "api_key=${MySetting.SauceNAOAPIKEY}&" +
                        "db=${MySetting.SauceNAODB}&" +
                        "numres=1&" +
                        "url=${URLDecoder.decode(image.queryUrl(), Charset.forName("utf-8"))}"
                )
            MiraiSetuMain.logger.info(json)
            parsejson(json)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(MySetting.SauceNAOAPIKEY, "/$/{APIKEY/}"))
            MiraiSetuMain.logger.error(e)
            throw e
        }
    }

    private fun parsejson(json: String) {
        val res: SaucenaoJson = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }.decodeFromString(json)
        result = res.results[0]
    }

    @KtorExperimentalAPI
    suspend fun sendmessage() {
        val image = client.get<InputStream>(result!!.header.thumbnail).uploadAsImage(subject)
        val msg = when (result!!.header.index_id) {
            // Index #5: Pixiv Images
            5 -> {
                "来源：Pixiv Images\n" +
                    "题目：${result!!.data.title}\n" +
                    "相似度：${result!!.header.similarity}\n" +
                    "pixiv id：${result!!.data.pixiv_id}\n" +
                    "作者：${result!!.data.member_name}\n" +
                    "作者id：${result!!.data.member_id}\n" +
                    "源链接：${result!!.data.ext_urls}"
            }
            // Index #21: Anime
            21 -> {
                "来源：Anime\n" +
                    "动画名：${result!!.data.source}\n" +
                    "相似度：${result!!.header.similarity}\n" +
                    "anidb_id：${result!!.data.pixiv_id}\n" +
                    "年代：${result!!.data.year}\n" +
                    "集数：${result!!.data.part}\n" +
                    "源链接：${result!!.data.ext_urls}"
            }
            // Index #34: deviantArt
            34 -> {
                "来源：deviantArt\n" +
                    "题目：${result!!.data.title}\n" +
                    "相似度：${result!!.header.similarity}\n" +
                    "图片id：${result!!.data.da_id}\n" +
                    "作者：${result!!.data.author_name}\n" +
                    "作者链接：${result!!.data.author_url}\n" +
                    "源链接：${result!!.data.ext_urls}"
            }
            40 -> {
                "来源：FurAffinity\n"
            }
            else -> "暂时无法解析的参数，数据库：${result!!.header.index_name}\n 请把开发者揪出来给他看看结果"
        }
        subject.sendMessage(PlainText(msg) + image)
    }
}
