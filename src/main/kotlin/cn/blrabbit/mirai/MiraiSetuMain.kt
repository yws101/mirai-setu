package cn.blrabbit.mirai

import cn.blrabbit.mirai.fantasyzone.Fantasyzone
import cn.blrabbit.mirai.fantasyzone.FantasyzoneEntrace
import cn.blrabbit.mirai.lolicon.SetuEntrance
import cn.blrabbit.mirai.lolicon.SetuImage
import cn.blrabbit.mirai.saucenao.Saucenao
import cn.blrabbit.mirai.utils.checkupdate
import cn.blrabbit.mirai.utils.storge.Command
import cn.blrabbit.mirai.utils.storge.Message
import cn.blrabbit.mirai.utils.storge.MySetting
import cn.blrabbit.mirai.utils.storge.Mydata
import io.ktor.util.*
import kotlinx.coroutines.async
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning

object Version {
    const val ID = "com.blrabbit.mirai-setu"
    const val PLUGINVERSION = "1.1.1-dev"
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
        // 自动检查更新
        async {
            checkupdate(version.toString())
        }
        MySetting.reload() //初始化设置数据
        Mydata.reload()    //初始化配置数据
        Command.reload()   //初始化插件指令
        Message.reload()   //初始化自定义回复
        // 指令注册
        // MySimpleCommand.register()
        if (MySetting.LoliconAPIKEY == "365007185fc06c84ac62e6") {
            logger.warning { "未设置lolicon的APIKEY，已经切换为公用apikey，可能会遇到调用上限的问题。\n请到(https://api.lolicon.app/#/setu)按照提示申请APIKEY并写入配置文件中。" }
        }
        SetuEntrance()
        SaucenaoEntrace()
        FantasyzoneEntrace()

        // logger.info { "色图插件加载完成，版本：$version" }

    }

    @KtorExperimentalAPI
    override fun onDisable() {
        // 关闭ktor客户端，防止堵塞线程无法关闭
        SetuImage.closeClient()
        Saucenao.closeClient()
        Fantasyzone.closeClient()
        logger.info { "色图插件已关闭，牛年快乐" }
    }
}

/*
object MySimpleCommand : SimpleCommand(
    MiraiSetuMain, "gui",
    description = "色图配置gui界面"
) {
    @Handler
    fun main() { // 函数名随意, 但参数需要按顺序放置.
        EventQueue.invokeLater(::createAndShowGUI)
    }
}

*/
