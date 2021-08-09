package org.example.mirai.plugin

import io.ktor.util.*
import moe.ruabbit.mirai.PluginMain
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@InternalAPI
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    PluginMain.load()
    PluginMain.enable()

//    测试请在此处添加qq号，或者手动登录
//    提交代码请注意删除账号信息避免泄露
    /*val bot = MiraiConsole.addBot(123456, "") {
        fileBasedDeviceInfo()
    }.alsoLogin()*/

    MiraiConsole.job.join()
}
