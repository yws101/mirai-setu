package com.blrabbit.mirai.setu

import com.blrabbit.mirai.APIKEY
import com.blrabbit.mirai.MiraiSetuMain
import com.blrabbit.mirai.Util.Mydata
import io.ktor.client.*
import io.ktor.client.engine.*
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
    suspend fun getsetu(){
        val setujson:String = HttpClient().use { client ->
            client.engine.config.proxy = ProxyBuilder.socks(host = "127.0.0.1", port = 10808)
            client.get("http://api.lolicon.app/setu?apikey=$APIKEY&size1200=true")
        }
        parseSetu(setujson)
    }

    suspend fun getsetu(keyword:String){
        val setujson:String = HttpClient().use { client ->
            client.get("http://api.lolicon.app/setu?apikey=$APIKEY")
        }
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
        return "pid：${pid}\n" +
            "title: ${title}\n" +
            "author: ${author}\n" +
            "url: ${url}\n" +
            "tags: ${tags}"
    }

    suspend fun downloadImage(): InputStream{
        return HttpClient().use { client ->
            client.get(url)
        }
    }

}