package cn.blrabbit.mirai.Util.storge

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Mydata: AutoSavePluginData("setu-data") {
    // 记录剩余的涩图请求量
    var quota by value(-1)

    // 记录群的权限
    var Grouppower: MutableMap<Long, Int> by value(mutableMapOf())
}