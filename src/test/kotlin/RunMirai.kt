package org.example.mirai.plugin

import com.blrabbit.mirai.MiraiSetuMain
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi


@ConsoleExperimentalApi
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    MiraiSetuMain.load()
    MiraiSetuMain.enable()

    /*val bot = MiraiConsole.addBot(876334393, "Yangyilin3600") {
        fileBasedDeviceInfo()
    }.alsoLogin()*/

    MiraiConsole.job.join()
}