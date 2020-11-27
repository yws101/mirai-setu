package com.blrabbit.mirai

import com.blrabbit.mirai.Util.*
import com.blrabbit.mirai.setu.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning

const val ID = "com.blrabbit.mirai-setu"
const val VERSION = "0.2.1"
const val NAME = "Mirai-setu"
const val AUTHOR = "blrabbit"
const val INFO = "一个简简单单的色图插件"

var APIKEY = "365007185fc06c84ac62e6"

object MiraiSetuMain : KotlinPlugin(
    JvmPluginDescription(
        id = ID,
        version = VERSION
    ) {
        name(NAME)
        author(AUTHOR)
        info(INFO)
    }
) {
    @KtorExperimentalAPI
    override fun onEnable() {
        MySetting.reload() //初始化设置数据
        Mydata.reload()    //初始化配置数据
        Command.reload()   //初始化插件指令
        Language.reload()
        logger.info { "色图插件加载完成，版本：$VERSION" }
        logger.info(MySetting.APIKEY)
        if (MySetting.APIKEY == "0") {
            logger.warning { "未设置lolicon的APIKEY，已经切换为公用apikey，可能会遇到调用上限的问题。\n请到(https://api.lolicon.app/#/setu)申请APIKEY并写入配置文件中。" }
            APIKEY = MySetting.APIKEY
        }
        if (MySetting.masterid.equals(0))
            logger.warning { "没有设置主人id" }
        subscribeGroupMessages {
            /*startsWith("搜图"){
                reply(Getillust(it))

            }*/
            case("舔我") {
                val msg: String = HttpClient(CIO).use { client ->
                    client.get("https://chp.shadiao.app/api.php")
                }
                if (!Language.tianwo.contains(msg))
                    Language.tianwo.add(msg)
                reply(At(sender) + " " + msg)
            }
            case("骂我") {
                val msg: String = HttpClient(CIO).use { client ->
                    client.get("https://nmsl.shadiao.app/api.php?level=min&lang=zh_cn")
                }
                if (!Language.zuan.contains(msg))
                    Language.zuan.add(msg)
                reply(At(sender) + " " + msg)
            }
            //昨日番剧
            case(Command.command_bangumiyesterday) {
                reply(bangumi_timeline(Command.command_bangumiyesterday, 5))
            }
            //今日番剧
            case(Command.command_bangumitoday) {
                reply(bangumi_timeline(Command.command_bangumitoday, 6))
            }
            //明日番剧
            case(Command.command_bangumitomorrow) {
                reply(bangumi_timeline(Command.command_bangumitomorrow, 7))
            }
            //色图时间
            case(Command.command_get) {
                if (!Mydata.groups.contains(group.id)) {
                    reply("此群没有色图权限")
                    return@case
                }
                /* if (Mydata.R18.contains(group.id)) group.sendMessage("正在获取图片中，请稍后\n[R18模式已启用]")
                else group.sendMessage("正在获取图片，请稍后")*/
                try {
                    val setu: Image = Json.decodeFromString(Getsetu(Mydata.R18.contains(group.id).toShort()))
                    reply(parseSetu(setu))
                    setu.data?.get(0)?.let { it2 -> sendImage(it2.let { it1 -> Downsetu(it1.url) }) }
                } catch (e: Exception) {
                    reply(e.toString())
                }
            }
            //搜色图
            startsWith(Command.command_search) {
                if (!Mydata.groups.contains(group.id)) {
                    reply("此群没有色图权限")
                    return@startsWith
                }
                if (it.isEmpty()) {
                    reply("请输入搜索的关键词")
                    return@startsWith
                }
                /* if (Mydata.R18.contains(group.id)) group.sendMessage("正在获取图片中，请稍后\n[R18模式已启用]")
                else group.sendMessage("正在获取图片，请稍后")*/
                try {
                    //val setu = Klaxon().parse<Image>(Getsetu(Mydata.R18.contains(group.id).toShort(), it))
                    val setu: Image = Json.decodeFromString(Getsetu(Mydata.R18.contains(group.id).toShort(), it))
                    reply(parseSetu(setu))
                    setu.data?.get(0)?.let { it2 -> sendImage(it2.let { it1 -> Downsetu(it1.url) }) }
                } catch (e: Exception) {
                    reply(e.toString())
                }
            }
            //开启R18模式增加的模式
            if (MySetting.R18) {
                //关闭R18搜索条件
                case(Command.command_R18off) {
                    if (!Mydata.groups.contains(group.id)) {
                        reply("此群没有色图权限")
                        return@case
                    }
                    reply("R18已关闭")
                    Mydata.R18.remove(group.id)
                }
                //开启R18搜索条件
                case(Command.command_R18on) {
                    if (!Mydata.groups.contains(group.id)) {
                        reply("此群没有色图权限")
                        return@case
                    }
                    reply("R18限制已解除")
                    Mydata.R18.add(group.id)
                }
            }
            /*case("状态检查") {
                sender.group.sendMessage("剩余调用次数：${Mydata.quota}\n" +
                    "当前R18模式 ${Mydata.R18.contains(group.id)}\n" +
                    "此群是否开启此插件${Mydata.groups.contains(group.id)}")
            }*/
            //封印解除
            case(Command.command_on) {
                if (sender.id == MySetting.masterid) {
                    reply("启用该群色图功能")
                    if (!Mydata.groups.contains(group.id))
                        Mydata.groups.add(group.id)
                } else
                    reply("你不是我的主人，我不能听从你的命令")
            }
            //封印
            case(Command.command_off) {
                if (sender.id == MySetting.masterid) {
                    reply("已禁用该群色图功能")
                    Mydata.groups.remove(group.id)
                } else
                    reply("你不是我的主人，我不能听从你的命令")
            }
        }
    }
}

//布尔型转short星自定义
private fun Boolean.toShort(): Short {
    return if (this) 1 else 0

}
