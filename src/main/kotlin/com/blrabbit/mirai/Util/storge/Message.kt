package com.blrabbit.mirai.Util.storge

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.value
import org.example.mirai.plugin.JsonData.LoliconJson

object Message : ReadOnlyPluginConfig("Message") {
    val SetuReply: String by value("pid: %pid%\ntitle: %title%\nauthor: %author%\nurl: %originalurl%\ntags: %tags%")
    val Lolicode401 by value("")
    val lolicode404 by value("")
    val lolicode429 by value("")
    val lolicodeelse by value("")
}