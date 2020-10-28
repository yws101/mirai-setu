package com.blrabbit.mirai.setu
//用来从lolicon获取链接

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


//获取一个lolicon返回的json文件

class Data(val pid: Int, val title: String, val author: String, val url: String, val tags: List<String>)
class Image(val code: Int, val quota: Int, val data: List<Data>)

/* json示例
{
	"code": 0, 返回码
	"msg": "", 错误提示
	"quota": 9, 调用剩余次数
	"quota_min_ttl": 7200, 不知道干啥的
	"count": 1, 数量
	"data": [{
		"pid": 78020086, pixiv的编码
		"p": 0, 不知道干啥的
		"uid": 221597, 貌似是他的数据库的编码
		"title": "うさちゃん", 题目
		"author": "はみこ", 作者
		"url": "https:\/\/i.pixiv.cat\/img-original\/img\/2019\/11\/27\/18\/42\/45\/78020086_p0.png", 图片链接
		"r18": false, 你懂的
		"width": 900, 图片大小
		"height": 1273,
		"tags": ["オリジナル", "原创", "女の子", "女孩子", "けもみみ", "兽耳", "下着", "内衣", "ツインテール", "双马尾", "うさみみ", "bunny ears", "ピンク髪", "粉色头发", "尻神様", "尻神样"]
	}]
}*/

fun Getsetu(R18: Int): String {
    val client = OkHttpClient()

    val request = Request.Builder().get()
        .url("https://api.lolicon.app/setu?apikey=${MySetting.APIKEY}&r18=$R18")
        .build()

    val call = client.newCall(request)

    return call.execute().body?.string().toString()
}


fun Getsetu(R18: Int, keyword: String): String {
    val client = OkHttpClient()

    val request = Request.Builder().get()
        .url("https://api.lolicon.app/setu?apikey=${MySetting.APIKEY}&r18=$R18&keyword=$keyword")
        .build()

    val call = client.newCall(request)
    return call.execute().body?.string().toString()

}


fun Downsetu(url: String): InputStream? {
    val client = OkHttpClient()

    val request = Request.Builder().get()
        .url(url)
        .build()
    val call = client.newCall(request)

    return call.execute().body?.byteStream()
}
