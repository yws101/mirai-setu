package com.blrabbit.mirai.Util

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Command: ReadOnlyPluginConfig("command") {
    val setucommand by value(Setucommand())
    @ValueDescription("插件命令的一些配置，可以自定义修改改变在群里发送的消息")
    val command_get by value(mutableListOf("色图时间","涩图时间","涩图来","色图来"))
    val command_search by value(mutableListOf("搜色图"))
    val command_on by value(mutableListOf("封印解除"))
    val command_off by value(mutableListOf("封印"))
    @ValueDescription("下面两条命令只有在启用R18的时候才会使用")
    val command_R18off by value(mutableListOf("青少年模式"))
    val command_R18on by value(mutableListOf("青壮年模式"))
    @ValueDescription("Bilibili番剧的指令设置")
    val command_bangumitoday by value(mutableListOf("今日番剧"))
    val command_bangumiyesterday by value(mutableListOf("昨日番剧"))
    val command_bangumitomorrow by value(mutableListOf("明日番剧"))

    @ValueDescription("以图搜番")
    val command_searchbangumibyimage by value(mutableListOf("以图搜番", "以图识番"))

    @Serializable
    data class Setucommand(
        val command_get: MutableList<String> = mutableListOf("色图时间", "涩图时间", "涩图来", "色图来"),
        val command_search: MutableList<String> = mutableListOf("搜色图", "搜涩图"),
        val command_on: MutableList<String> = mutableListOf("封印解除"),
        val command_off: MutableList<String> = mutableListOf("封印")
    )

    @Serializable
    data class BiliBilicommand(
        val command_bangumitoday: MutableList<String> = mutableListOf("今日番剧"),
        val command_bangumiyesterday: String = ("昨日番剧"),
        val command_bangumitomorrow: MutableList<String> = mutableListOf("明日番剧"),
        val command_searchbangumibyimage: MutableList<String> = mutableListOf("以图搜番", "以图识番")
    )

}