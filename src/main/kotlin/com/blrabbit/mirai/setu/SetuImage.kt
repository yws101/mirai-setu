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
import org.example.mirai.plugin.JsonData.LoliconJson
import java.io.InputStream

class SetuImage() {
    // 图片数据
    var pid: Int = 0
    var p: Int = 0
    var uid: Int = 0
    var title: String = ""
    var author: String= ""
    var url: String = ""
    var r18: Boolean = false
    var width: Int = 0
    var height: Int = 0
    var tags: List<String> = listOf()

    @KtorExperimentalAPI
    val client = HttpClient(OkHttp){

        engine {
            proxy = when(MySetting.proxyconfig){
                0 -> null
                1 -> ProxyBuilder.http(MySetting.httpproxy.proxy)
                2 -> ProxyBuilder.socks(host = MySetting.socksproxy.host,port = MySetting.socksproxy.port)
                else -> null
            }
        }
    }

    @KtorExperimentalAPI
    suspend fun getsetu(){
        val setujson:String = client.get("http://api.lolicon.app/setu?apikey=$APIKEY&size1200=true")
        parseSetu(setujson)
    }

    @KtorExperimentalAPI
    suspend fun getsetu(keyword:String){
        val setujson:String = client.get("http://api.lolicon.app/setu?apikey=$APIKEY")
        parseSetu(setujson)
    }

    private fun parseSetu(setujson:String){
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
                url = it.url
                r18 = it.r18
                width = it.width
                height = it.height
                tags = it.tags
            }
        } else
            throw Exception(result.code.toString() + result.msg)
    }

    fun getstr(): String {
        //TODO 修改为能够自定义返回内容的方法，参考mc的占位符
        return "pid：${pid}\n" +
            "title: ${title}\n" +
            "author: ${author}\n" +
            "url: ${url}\n" +
            "tags: ${tags}"
    }

    @KtorExperimentalAPI
    suspend fun getoriginalImage(): InputStream{
        return HttpClient().use { client ->
            client.get(url)
        }
    }

    @KtorExperimentalAPI
    suspend fun getlargeImage(): InputStream{
        //TODO 经常失效404，尝试寻找一个更稳定的方法，临时方案在源链接访问失败的情况下访问源链接
        val urls = url.replace("img-original","c/600x1200_90_webp/img-master").replace(".jpg","_master1200.jpg")
        return try {
            client.get(urls)
        }catch (e: ClientRequestException){
            //MiraiSetuMain.logger.warning("获取缩略图失败，尝试获取原始图片")
            client.get(url)
        }
    }
    // httpclient一定要关闭，否则会一直驻留在内存中。不断创建直到内存溢出
    @KtorExperimentalAPI
    fun close(){
        client.close()
    }
}
