package cn.blrabbit.mirai.setu

import cn.blrabbit.mirai.MiraiSetuMain
import cn.blrabbit.mirai.Util.storge.Message
import cn.blrabbit.mirai.Util.storge.MySetting
import cn.blrabbit.mirai.Util.storge.Mydata
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.error
import org.example.mirai.plugin.JsonData.LoliconJson
import java.io.InputStream

@KtorExperimentalAPI
private val client = HttpClient(OkHttp) {
    engine {
        proxy = when (MySetting.proxyconfig) {
            0 -> null
            1 -> ProxyBuilder.http(MySetting.httpproxy.proxy)
            2 -> ProxyBuilder.socks(host = MySetting.socksproxy.host, port = MySetting.socksproxy.port)
            else -> null
        }
    }
}

@KtorExperimentalAPI
fun closeClient() {
    client.close()
}

//直连pixiv的示例
/*
@KtorExperimentalAPI
suspend fun getpixiv(): InputStream {
    return client.get("https://i.pximg.net/img-original/img/2021/01/17/00/30/01/87098740_p0.jpg") {
        headers.append("referer", "https://www.pixiv.net/")
    }
}*/
class SetuImage(val subject: Contact) {
    // 图片数据
    var pid: Int = 0
    var p: Int = 0
    var uid: Int = 0
    var title: String = ""
    var author: String = ""
    var originalurl: String = ""
    var largeurl: String = ""
    var r18: Boolean = false
    var width: Int = 0
    var height: Int = 0
    var tags: List<String> = listOf()

    @KtorExperimentalAPI
    suspend fun getsetu() {
        try {
            val setujson: String =
                client.get("http://api.lolicon.app/setu?apikey=${MySetting.APIKEY}&r18=${Mydata.Grouppower[subject.id]}")
            parseSetu(setujson)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(MySetting.APIKEY, "/$/{APIKEY/}"))
            MiraiSetuMain.logger.error(e)
            throw e
        }
    }

    @KtorExperimentalAPI
    suspend fun getsetu(keyword: String) {
        try {
            val setujson: String =
                client.get("http://api.lolicon.app/setu?apikey=${MySetting.APIKEY}&keyword=${keyword}&r18=${Mydata.Grouppower[subject.id]}\"")
            parseSetu(setujson)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(MySetting.APIKEY, "/$/{APIKEY/}"))
            MiraiSetuMain.logger.error(e)
            throw e
        }
    }

    private fun parseSetu(setujson: String) {
        val result: LoliconJson = Json.decodeFromString(setujson)
        fun parsecode(message: String): String {
            return message
                .replace("%code%", result.code.toString())
                .replace("%msg%", result.msg)
        }

        when (result.code) {
            0 -> {
                Mydata.quota = result.quota
                result.data?.get(0)?.let {
                    MiraiSetuMain.logger.info("剩余调用次数 ${result.quota}")
                    pid = it.pid
                    p = it.p
                    uid = it.uid
                    title = it.title
                    author = it.author
                    originalurl = it.url
                    r18 = it.r18
                    width = it.width
                    height = it.height
                    tags = it.tags
                    //拼装成缩略图URL
                    largeurl = originalurl.replace("img-original", "c/600x1200_90_webp/img-master")
                        .replace(".png", ".jpg")
                        .replace(".jpg", "_master1200.jpg")
                }
            }
            // API错误
            401 -> {
                MiraiSetuMain.logger.error { "lolicon错误代码：${result.code} 错误信息：${result.msg}" }
                throw Exception(parsecode(Message.Lolicode401))
            }
            // 色图搜索404
            404 -> {
                MiraiSetuMain.logger.error { "lolicon错误代码：${result.code} 错误信息：${result.msg}" }
                throw Exception(parsecode(Message.lolicode404))
            }
            // 调用到达上限
            429 -> {
                MiraiSetuMain.logger.error { "lolicon错误代码：${result.code} 错误信息：${result.msg}" }
                throw Exception(parsecode(Message.lolicode429))
            }
            // -1和403 错误等一系列未知错误
            else -> {
                MiraiSetuMain.logger.error { "发生此错误请到github反馈错误 lolicon错误代码：${result.code} 错误信息：${result.msg}" }
                throw Exception(parsecode(Message.lolicodeelse))
            }
        }
    }

    private fun parsemessage(message: String): String {
        return message
            .replace("%pid%", pid.toString())
            .replace("%p%", p.toString())
            .replace("%uid%", uid.toString())
            .replace("%title%", title)
            .replace("%author%", author)
            .replace("%originalurl%", originalurl)
            .replace("%r18%", r18.toString())
            .replace("%width%", width.toString())
            .replace("%height%", height.toString())
            .replace("%tags%", tags.toString())
            .replace("%largeurl", largeurl)
    }

    suspend fun getstr() {
        subject.sendMessage(parsemessage(Message.SetuReply))
    }

    @KtorExperimentalAPI
    suspend fun getoriginalImage(): InputStream {
        return client.get(originalurl.replace("i.pixiv.cat", MySetting.domainproxy)) {
            headers.append("referer", "https://www.pixiv.net/")
        }
    }

    @KtorExperimentalAPI
    suspend fun getlargeImage(): InputStream {
        return client.get(largeurl.replace("i.pixiv.cat", MySetting.domainproxy)) {
            headers.append("referer", "https://www.pixiv.net/")
        }
    }

    @KtorExperimentalAPI
    suspend fun sendsetu() {
        try {
            subject.sendImage(getlargeImage())
        } catch (e: ClientRequestException) {
            try {
                //sleep(1000) //似乎经常错误，停顿一下？没搞明白
                subject.sendImage(getoriginalImage())
            } catch (e: ClientRequestException) {
                subject.sendMessage(Message.image404)
            }
        } catch (e: Exception) {
            subject.sendMessage("出现错误" + e.message?.replace(MySetting.APIKEY, "/$/{APIKEY/}"))
            MiraiSetuMain.logger.error(e)
            throw e
        }
    }

}
