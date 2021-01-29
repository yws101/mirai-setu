package com.blrabbit.mirai.setu

import com.blrabbit.mirai.APIKEY
import com.blrabbit.mirai.MiraiSetuMain
import com.blrabbit.mirai.Util.MySetting
import com.blrabbit.mirai.Util.Mydata
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.command.ConsoleCommandSender.subject
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Group
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
    suspend fun getsetu(){
        val setujson:String = client.get("http://api.lolicon.app/setu?apikey=$APIKEY&size1200=true")
        parseSetu(setujson)
    }

    @KtorExperimentalAPI
    suspend fun getsetu(keyword:String){
        val setujson:String = client.get("http://api.lolicon.app/setu?apikey=$APIKEY&keyword=${keyword}")
        parseSetu(setujson)
    }

    private suspend fun parseSetu(setujson: String) {
        val result: LoliconJson = Json.decodeFromString(setujson)
        if (result.code == 0) {
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
                largeurl = originalurl.replace("img-original", "c/600x1200_90_webp/img-master")
                    .replace(".jpg", "_master1200.jpg")
            }
        } else {
            MiraiSetuMain.logger.error { "lolicon错误代码：${result.code} 错误信息：${result.msg}" }
            subject.sendMessage("lolicon错误代码：${result.code}\n 错误信息：${result.msg}")
            throw Exception("lolicon错误代码：${result.code} 错误信息：${result.msg}")
        }
    }

    suspend fun getstr() {
        //TODO 修改为能够自定义返回内容的方法，参考mc的占位符
        subject.sendMessage(
            "pid：${pid}\n" +
                "title: ${title}\n" +
                "author: ${author}\n" +
                "url: ${originalurl}\n" +
                "tags: ${tags}"
        )
    }

    @KtorExperimentalAPI
    suspend fun getoriginalImage(): InputStream{
        return HttpClient().use { client ->
            client.get(originalurl)
        }
    }

    @KtorExperimentalAPI
    suspend fun getlargeImage(): InputStream {
        //TODO 经常失效404，尝试寻找一个更稳定的方法，临时方案在源链接访问失败的情况下访问源链接
        return client.get(largeurl)
    }

    @KtorExperimentalAPI
    suspend fun sendsetu() {
        try {
            subject.sendImage(getlargeImage())
        } catch (e: ClientRequestException) {
            try {
                subject.sendImage(getoriginalImage())
            } catch (e: ClientRequestException) {
                subject.sendMessage("图片获取失败，可能图片已经被原作者删除")
            }
        } catch (e: Exception) {
            subject.sendMessage("出现错误，请到控制台查看")
            MiraiSetuMain.logger.error(e)
        }
    }

}
