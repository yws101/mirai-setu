package com.blrabbit.mirai.setu

import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.utils.BotConfiguration
import java.util.*

suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()


    PluginMain.load()
    PluginMain.enable()


    val bot = MiraiConsole.addBot(123456, "") {
        //protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()
}