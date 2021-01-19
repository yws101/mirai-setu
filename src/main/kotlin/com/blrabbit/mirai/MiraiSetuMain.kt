package com.blrabbit.mirai

import com.blrabbit.mirai.Util.Command
import com.blrabbit.mirai.Util.MySetting
import com.blrabbit.mirai.Util.Mydata
import com.blrabbit.mirai.bilibili.BiliBiliEntrace
import com.blrabbit.mirai.setu.SetuEntrance
import io.ktor.util.*
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning

var APIKEY = "365007185fc06c84ac62e6"

object Version{
    const val ID = "com.blrabbit.mirai-setu"
    const val PLUGINVERSION = "0.2.3"
    const val NAME = "Mirai-setu"
}

object MiraiSetuMain : KotlinPlugin(
    JvmPluginDescription(
        id = Version.ID,
        name = Version.NAME,
        version = Version.PLUGINVERSION
    )
) {
    @KtorExperimentalAPI
    override fun onEnable() {
        MySetting.reload() //初始化设置数据
        Mydata.reload()    //初始化配置数据
        Command.reload()   //初始化插件指令
        if (MySetting.APIKEY == "0") {
            logger.warning { "未设置lolicon的APIKEY，已经切换为公用apikey，可能会遇到调用上限的问题。\n请到(https://api.lolicon.app/#/setu)申请APIKEY并写入配置文件中。" }
            APIKEY = MySetting.APIKEY
        }
        if (MySetting.masterid.equals(0)) {
            logger.warning { "没有设置主人id" }
        }
        SetuEntrance()
        BiliBiliEntrace()
        logger.info { "色图插件加载完成，版本：$version Java版本:${System.getProperty("java.version")}" }
    }
}
