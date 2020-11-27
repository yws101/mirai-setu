package com.blrabbit.mirai.Util

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Language : AutoSavePluginData("Language") {
    var tianwo:MutableList<String> by value()
    var zuan:MutableList<String> by value()
}