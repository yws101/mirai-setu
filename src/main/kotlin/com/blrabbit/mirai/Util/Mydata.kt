package com.blrabbit.mirai.Util

import com.blrabbit.mirai.Util.Mydata.provideDelegate
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Mydata: AutoSavePluginData("setu-data") {
    var quota by value(-1)
    var R18: MutableList<Long> by value()
    var groups: MutableList<Long> by value()
}