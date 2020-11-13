package com.blrabbit.mirai.setu

import com.beust.klaxon.Klaxon
import okhttp3.OkHttpClient
import okhttp3.Request


class BiliData(
    val code: Int,
    val message: String,
    val result: List<Result>
)

class Result(
    val date: String,
    val date_ts: Int,
    val day_of_week: Int,
    val is_today: Int,
    val seasons: List<Seasons>
)

class Seasons(
    //val cover: String,
    val delay: Int,
    val delay_reason: String = "",
    //val ep_id: Int,
    val is_published: Int,
    val pub_index: String = "",
    val pub_time: String,
    //val pub_ts: Int,
    //val season_id: Int,
    //val season_status: Int,
    //val square_cover: String,
    val title: String,
    //val url: String
)

fun GetBiliTimeline(s: String, i: Int): String {

    val request = Request.Builder().get()
        .url("https://bangumi.bilibili.com/web_api/timeline_global")
        .build()

    val call = client.newCall(request)

    val bilidata: String = call.execute().body?.string().toString()

    val result = Klaxon().parse<BiliData>(bilidata)

    var msg: String = "今日番剧\n"
    if (result != null) {
        for (index in result.result[6].seasons) {
            msg += "---------------\n"
            msg += "${index.title} ${index.pub_index}\n更新时间:${index.pub_time}"
            msg += if (index.delay == 0) {
                if (index.is_published == 1)
                    "(已更新)\n"
                else
                    "(未更新)\n"
            } else {
                "(${index.delay_reason})\n"
            }
        }
    }
    return msg
}

fun main() {
    /*val formatter = SimpleDateFormat("MM-dd")
    val date = Date(System.currentTimeMillis())
    println(formatter.format(date))
    println(date)*/
    print(GetBiliTimeline("昨日番剧", 6))
}