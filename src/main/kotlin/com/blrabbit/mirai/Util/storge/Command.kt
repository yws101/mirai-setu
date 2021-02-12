package com.blrabbit.mirai.Util.storge

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.value

object Command : ReadOnlyPluginConfig("command") {
    val setucommand by value(Setucommand())
    val bilibilicommand by value(BiliBilicommand())

    @Serializable
    data class Setucommand(
        val command_get: MutableList<String> = mutableListOf("色图时间", "涩图时间", "涩图来", "色图来"),
        val command_search: MutableList<String> = mutableListOf("搜色图", "搜涩图"),
        val command_off: MutableList<String> = mutableListOf("封印"),
        val command_setumode0: MutableList<String> = mutableListOf("青少年模式"),
        val command_setumode1: MutableList<String> = mutableListOf("青壮年模式"),
        val command_setumode2: MutableList<String> = mutableListOf("混合模式")
    )

    @Serializable
    data class BiliBilicommand(
        val command_bangumitoday: MutableList<String> = mutableListOf("今日番剧"),
        val command_bangumiyesterday: MutableList<String> = mutableListOf("今日番剧"),
        val command_bangumitomorrow: MutableList<String> = mutableListOf("明日番剧"),
        val command_searchbangumibyimage: MutableList<String> = mutableListOf("以图搜番", "以图识番")
    )

}