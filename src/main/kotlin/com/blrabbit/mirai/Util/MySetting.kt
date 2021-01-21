package com.blrabbit.mirai.Util

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MySetting : ReadOnlyPluginConfig("setu-config") {
    @ValueDescription("设置此插件主人的id。")
    var masterid by value(mutableListOf<Long>(123456))
    @ValueDescription("设置lolicon的APIKEY，可以不设置，但是每天调用次数会比较少。")
    var APIKEY by value("365007185fc06c84ac62e6")
    @ValueDescription("是否开启R18的功能，改为true启用。开启后漏点图比较多，比较容易封号，建议关闭。")
    val R18 by value(false)
    @ValueDescription("代理设置,0为不使用代理，1为使用http代理，2为使用socks代理")
    val proxyconfig by value(0)

}
