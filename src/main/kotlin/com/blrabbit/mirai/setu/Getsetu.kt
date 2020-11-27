package com.blrabbit.mirai.setu
//用来从lolicon获取链接

import com.blrabbit.mirai.APIKEY
import com.blrabbit.mirai.MiraiSetuMain
import com.blrabbit.mirai.Util.MySetting
import com.blrabbit.mirai.Util.Mydata
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import java.io.InputStream

@Serializable
data class Image(
    val code: Int,
    val msg: String = "",
    val quota: Int,
    val quota_min_ttl: Int,
    val count: Int,
    val data: List<Data>? = null
)

@Serializable
data class Data(
    val pid: Int,
    val p: Int,
    val uid: Int,
    val title: String,
    val author: String,
    val url: String,
    val r18: Boolean,
    val width: Int,
    val height: Int,
    val tags: List<String>
)

@KtorExperimentalAPI
suspend fun Getsetu(R18: Short): String {
    return HttpClient(CIO).use { client ->
        client.get("http://api.lolicon.app/setu?apikey=$APIKEY&r18=${R18}")
    }
}

@KtorExperimentalAPI
suspend fun Getsetu(R18: Short, keyword: String): String {
    return HttpClient(CIO).use { client ->
        client.get("http://api.lolicon.app/setu?apikey=$APIKEY&r18=$R18&keyword=$keyword")
    }
}

@KtorExperimentalAPI
suspend fun Downsetu(url: String): InputStream {
    return HttpClient(CIO).use { client ->
        client.get(url)
    }
}

fun parseSetu(result: Image): String {
    if (result.code == 0) {
        Mydata.quota = result.quota
        result.data?.get(0)?.let {
            print(it.pid)
            if (it.tags.contains("R-18") && !MySetting.R18) {
                throw Exception("检测到敏感资源拒绝发送")
            }
            MiraiSetuMain.logger.info("剩余调用次数 ${result.quota}")
            return "pid：${it.pid}\n" +
                "title: ${it.title}\n" +
                "author: ${it.author}\n" +
                "url: ${it.url}\n" +
                "tags: ${it.tags}"
        }
        return ""
    } else
        throw Exception(result.msg)
}

/*
fun Getsetu0(): String {
    val request = Request.Builder().get()
        .url("https://www.fantasyzone.cc/api/tu/?type=r18")
        .build()

    val call = client.newCall(request)
    return call.execute().body?.string().toString().drop(30).dropLast(29)
}
*/

//fun main() {
//val request = Request.Builder().get()
//        .url("https://i.pixiv.cat/img-original/img/2019/11/27/18/42/45/78020086_p0.png")
//        .build()
//    val call = client.newCall(request)
//
//    println("Done")
//
//    val ii = call.execute().body?.byteStream()
//
//    var len = 0
//    val file = File("data/n.png")
//    val fos = FileOutputStream(file)
//    val buf = ByteArray(1024)
//
//    if (ii != null) {
//        while (ii.read(buf).also { len = it } != -1) {
//            fos.write(buf, 0, len)
//        }
//    }
//
//    fos.flush()
//    //关闭流
//    //关闭流
//    fos.close()
//    ii?.close()*//*
//
//
//    print(Getsetu0())
//
//}
