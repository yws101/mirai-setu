package cn.blrabbit.mirai.utils

import cn.blrabbit.mirai.MiraiSetuMain
import io.ktor.client.request.*

suspend fun checkupdate(version: String) {
    val newversion: String =
        KtorUtils.normalclient.get("https://cdn.jsdelivr.net/gh/bloodyrabbit/mirai-setu@main/doc/Version.txt")

    // git给文件加了一个回车我能怎么办呢
    if (newversion.equals(version + "\n"))
        MiraiSetuMain.logger.info("色图插件当前版本:$version")
    else
        MiraiSetuMain.logger.warning("色图插件当前版本：$version，检查到新版本：$newversion 请到 https://github.com/bloodyrabbit/mirai-setu/releases 更新")
}
