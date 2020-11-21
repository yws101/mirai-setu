package com.blrabbit.mirai.Util

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object MySetting : AutoSavePluginConfig("setu-config") {
    val masterid: Long by value()
    val APIKEY by value("")
    val R18 by value(false)
    // val DateLocal by value(true)
}
