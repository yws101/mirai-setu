package cn.blrabbit.mirai

import cn.blrabbit.mirai.Util.storge.Command
import cn.blrabbit.mirai.Util.storge.Message
import cn.blrabbit.mirai.Util.storge.MySetting
import cn.blrabbit.mirai.Util.storge.Mydata
import cn.blrabbit.mirai.setu.SetuEntrance
import cn.blrabbit.mirai.setu.closeClient
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.util.*
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning
import java.io.InputStream

object Version{
    const val ID = "com.blrabbit.mirai-setu"
    const val PLUGINVERSION = "1.0.0"
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
        Message.reload()   //初始化自定义回复
        if (MySetting.APIKEY == "365007185fc06c84ac62e6") {
            logger.warning { "未设置lolicon的APIKEY，已经切换为公用apikey，可能会遇到调用上限的问题。\n请到(https://api.lolicon.app/#/setu)按照提示申请APIKEY并写入配置文件中。" }
        }
        SetuEntrance()
        //BiliBiliEntrace()
        logger.info { "色图插件加载完成，版本：$version" }
    }

    @KtorExperimentalAPI
    override fun onDisable() {
        closeClient()
        logger.info { "色图插件已关闭，牛年快乐" }
    }
}
