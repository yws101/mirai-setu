package com.blrabbit.mirai.Util

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MySetting : AutoSavePluginConfig("setu-config") {
    @ValueDescription("设置此插件主人的id。")
    var masterid: Long by value()
    @ValueDescription("设置lolicon的APIKEY，可以不设置，但是每天调用次数会比较少。")
    var APIKEY by value("0")
    @ValueDescription("是否开启R18的功能，改为true启用。开启后漏点图比较多，比较容易封号，建议关闭。")
    val R18 by value(false)
    // val DateLocal by value(true)
}
