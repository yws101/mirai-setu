package moe.ruabbit.mirai.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object SetuData : AutoSavePluginData("setu-data") {
    // 记录剩余的涩图请求量
    var quota by value(-1)

    // 记录群的色图策略
    var groupPolicy: MutableMap<Long, Int> by value(mutableMapOf())
}
