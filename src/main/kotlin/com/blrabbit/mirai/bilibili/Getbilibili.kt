package com.blrabbit.mirai.setu

import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.util.*

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
@KtorExperimentalAPI
suspend fun bangumi_timeline(title: String, i :Int): String {
    var json = ""
    try {
        json = HttpClient(CIO).use { client ->
            client.get<String>("https://bangumi.bilibili.com/web_api/timeline_global")
        }
    }catch (e:Exception){
        return e.toString()
    }
    val result = Klaxon().parse<BiliData>(json)
    var msg = "$title\n"
    if (result != null) {
        for (index in result.result[i].seasons) {
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


