package com.blrabbit.mirai.Util.storge

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.value
import org.example.mirai.plugin.JsonData.LoliconJson

object Message : ReadOnlyPluginConfig("Message") {
    val SetuReply: String by value("pid: %pid%\ntitle: %title%\nauthor: %author%\nurl: %originalurl%\ntags: %tags%")
    val Lolicode401 by value("lolicon错误代码：%code% 错误信息：%msg%")
    val lolicode404 by value("lolicon错误代码：%code% 错误信息：%msg%")
    val lolicode429 by value("lolicon错误代码：%code% 错误信息：%msg%")
    val lolicodeelse by value("lolicon错误代码：%code%,发生此错误请截取详细信息到github提交issue 错误信息：%msg%")
    val image404 by value("图片获取失败，可能图片已经被原作者删除")
}