package cn.blrabbit.mirai.Util.storge

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Mydata: AutoSavePluginData("setu-data") {
    var quota by value(-1)
    var Grouppower: MutableMap<Long, Int> by value(mutableMapOf())
}