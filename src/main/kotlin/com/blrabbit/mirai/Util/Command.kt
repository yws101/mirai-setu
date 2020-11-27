package com.blrabbit.mirai.Util

import com.blrabbit.mirai.Util.Command.provideDelegate
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Command: AutoSavePluginConfig("command") {
    @ValueDescription("插件命令的一些配置，可以自定义修改改变在群里发送的消息")
    val command_get by value("色图时间")
    val command_search by value("搜色图")
    val command_on by value("封印解除")
    val command_off by value("封印")
    @ValueDescription("下面两条命令只有在启用R18的时候才会使用")
    val command_R18off by value("青少年模式")
    val command_R18on by value("青壮年模式")
    @ValueDescription("Bilibili番剧的指令设置")
    val command_bangumitoday by value("今日番剧")
    val command_bangumiyesterday by value("昨日番剧")
    val command_bangumitomorrow by value("明日番剧")
}