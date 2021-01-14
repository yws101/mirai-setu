package com.blrabbit.mirai

import com.blrabbit.mirai.Util.*
import com.blrabbit.mirai.pixiv.Getillust
import com.blrabbit.mirai.pixiv.trace.serchmoe
import com.blrabbit.mirai.setu.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
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
        SetSetuKey.register() //注册指令
        SetMasterID.register() //注册指令
        MySetting.reload() //初始化设置数据
        Mydata.reload()    //初始化配置数据
        Command.reload()   //初始化插件指令
        logger.info { "色图插件加载完成，版本：$VERSION" }
        if (MySetting.APIKEY == "0") {
            logger.warning { "未设置lolicon的APIKEY，已经切换为公用apikey，可能会遇到调用上限的问题。\n请到(https://api.lolicon.app/#/setu)申请APIKEY并写入配置文件中。" }
            APIKEY = MySetting.APIKEY
        }
        if (MySetting.masterid.equals(0))
            logger.warning { "没有设置主人id" }
        GlobalEventChannel.subscribeGroupMessages {
            startsWith("以图搜番"){
                if(it.isEmpty())
                    group.sendMessage("搜索信息空")
                    group.sendMessage("测试功能，可能存在不稳定")
                try {
                    val a = message[Image]
                    if (a != null) {
                        logger.info(a.queryUrl())
                        serchmoe(a.queryUrl(), group)
                    }
                }catch (e:Exception){
                    group.sendMessage(e.toString())
                }
            }
            startsWith("搜图"){
                group.sendMessage(Getillust(it))
            }
            //昨日番剧
            case(Command.command_bangumiyesterday) {
                logger.info("请求昨日番剧")
                group.sendMessage(bangumi_timeline(Command.command_bangumiyesterday, 5))
                group.sendMessage(bangumi_timeline_cn(Command.command_bangumiyesterday, 5))
            }
            //今日番剧
            case(Command.command_bangumitoday) {
                logger.info("请求今日番剧")
                group.sendMessage(bangumi_timeline(Command.command_bangumitoday, 6))
                group.sendMessage(bangumi_timeline_cn(Command.command_bangumitoday, 6))
            }
            //明日番剧
            case(Command.command_bangumitomorrow) {
                logger.info("请求明日番剧")
                group.sendMessage(bangumi_timeline(Command.command_bangumitomorrow, 7))
                group.sendMessage(bangumi_timeline_cn(Command.command_bangumitomorrow, 7))
            }

            //色图时间
            case(Command.command_get) {

                if (!Mydata.groups.contains(group.id)) {

                    group.sendMessage("此群没有色图权限")
                    return@case
                }
                try {
                    val setu: SetuImage = Json.decodeFromString(Getsetu(Mydata.R18.contains(group.id).toShort()))
                    group.sendMessage(parseSetu(setu))
                    setu.data?.get(0)?.let { it2 -> group.sendImage(it2.let { it1 -> Downsetu(it1.url) }) }
                } catch (e: Exception) {
                    e.message?.let { it1 -> group.sendMessage(it1) }
                    logger.error(e)
                }
            }
            //搜色图
            startsWith(Command.command_search) {
                if (!Mydata.groups.contains(group.id)) {
                    group.sendMessage("此群没有色图权限")
                    return@startsWith
                }
                if (it.isEmpty()) {
                    group.sendMessage("请输入搜索的关键词")
                    return@startsWith
                }
                try {
                    val setu: SetuImage = Json.decodeFromString(Getsetu(Mydata.R18.contains(group.id).toShort(), it))
                    group.sendMessage(parseSetu(setu))
                    setu.data?.get(0)?.let { it2 -> group.sendImage(it2.let { it1 -> Downsetu(it1.url) }) }
                } catch (e: Exception) {
                    e.message?.let { it1 -> group.sendMessage(it1) }
                    logger.error(e)
                }
            }
            //开启R18模式增加的模式
            if (MySetting.R18) {
                //关闭R18搜索条件
                case(Command.command_R18off) {
                    if (!Mydata.groups.contains(group.id)) {
                        group.sendMessage("此群没有色图权限")
                        return@case
                    }
                    group.sendMessage("R18已关闭")
                    Mydata.R18.remove(group.id)
                }
                //开启R18搜索条件
                case(Command.command_R18on) {
                    if (!Mydata.groups.contains(group.id)) {
                        group.sendMessage("此群没有色图权限")
                        return@case
                    }
                    group.sendMessage("R18限制已解除")
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
                    group.sendMessage("启用该群色图功能")
                    if (!Mydata.groups.contains(group.id))
                        Mydata.groups.add(group.id)
                } else
                    group.sendMessage("你不是我的主人，我不能听从你的命令")
            }
            //封印
            case(Command.command_off) {
                if (sender.id == MySetting.masterid) {
                    group.sendMessage("已禁用该群色图功能")
                    Mydata.groups.remove(group.id)
                } else
                    group.sendMessage("你不是我的主人，我不能听从你的命令")
            }
        }
    }

}

//布尔型转short星自定义
private fun Boolean.toShort(): Short {
    return if (this) 1 else 0

}
