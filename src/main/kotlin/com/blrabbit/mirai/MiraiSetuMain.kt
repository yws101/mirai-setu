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
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.NudgeEvent
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning
import java.lang.reflect.Member

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
        //SetSetuKey.register() //注册指令
        //SetMasterID.register() //注册指令
        MySetting.reload() //初始化设置数据
        Mydata.reload()    //初始化配置数据
        Command.reload()   //初始化插件指令
        logger.info { "色图插件加载完成，版本：$version Java版本:${System.getProperty("java.version")}" }
        if (MySetting.APIKEY == "0") {
            logger.warning { "未设置lolicon的APIKEY，已经切换为公用apikey，可能会遇到调用上限的问题。\n请到(https://api.lolicon.app/#/setu)申请APIKEY并写入配置文件中。" }
            APIKEY = MySetting.APIKEY
        }
        if (MySetting.masterid.equals(0)) {
            logger.warning { "没有设置主人id" }

        }

        GlobalEventChannel.subscribeGroupMessages {
            always {
                if(message.contentToString().startsWith(Command.command_searchbangumibyimage)) {
                    if (it.isEmpty())
                        group.sendMessage("搜索信息空")
                    group.sendMessage("测试功能，可能存在不稳定")
                    try {
                        val a = message[Image]
                        if (a != null) {
                            logger.info(a.queryUrl())
                            serchmoe(a.queryUrl(), group)
                        }
                    } catch (e: Exception) {
                        group.sendMessage(e.toString())
                    }
                }
            }
            //昨日番剧
            always {
                if(Command.command_bangumiyesterday.contains(message.contentToString())) {
                    try {
                        logger.info("请求昨日番剧")
                        group.sendMessage(GetbangumiTimeline(message.contentToString(), 5))
                    } catch (e: Exception) {
                        group.sendMessage("出现异常，请到控制台查看详细错误信息\n${e.message}")
                        logger.error(e)
                    }
                }
            }
            //今日番剧
            always {
                if(Command.command_bangumitoday.contains(message.contentToString())) {
                    try {

                        group.sendMessage(GetbangumiTimeline(message.contentToString(), 6))
                    } catch (e: Exception) {
                        group.sendMessage("出现异常，请到控制台查看详细错误信息\n${e.message}")
                        logger.error(e)
                    }
                }
            }
            //明日番剧
            always {
                if(Command.command_bangumitomorrow.contains(message.contentToString())) {
                    try {
                        logger.info("请求明日番剧")
                        group.sendMessage(GetbangumiTimeline(message.contentToString(), 7))
                    } catch (e: Exception) {
                        group.sendMessage("出现异常，请到控制台查看详细错误信息\n${e.message}")
                        logger.error(e)
                    }
                }
            }

            //色图时间
            always() {
                if(Command.command_get.contains(message.contentToString())) {
                    if (!Mydata.groups.contains(group.id)) {

                        group.sendMessage("此群没有色图权限")
                        return@always
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
            }
            //搜色图
            always {
                if(message.contentToString().startsWith(Command.command_search)) {
                    if (!Mydata.groups.contains(group.id)) {
                        group.sendMessage("此群没有色图权限")
                        return@always
                    }
                    if (it.isEmpty()) {
                        group.sendMessage("请输入搜索的关键词")
                        return@always
                    }
                    try {
                        val setu: SetuImage =
                            Json.decodeFromString(Getsetu(Mydata.R18.contains(group.id).toShort(), it))
                        group.sendMessage(parseSetu(setu))
                        setu.data?.get(0)?.let { it2 -> group.sendImage(it2.let { it1 -> Downsetu(it1.url) }) }
                    } catch (e: Exception) {
                        e.message?.let { it1 -> group.sendMessage(it1) }
                        logger.error(e)
                    }
                }
            }
            //开启R18模式增加的模式
            if (MySetting.R18) {
                //关闭R18搜索条件
                always {
                    if(Command.command_R18off.contains(message.contentToString())) {
                        if (!Mydata.groups.contains(group.id)) {
                            group.sendMessage("此群没有色图权限")
                            return@always
                        }
                        group.sendMessage("R18已关闭")
                        Mydata.R18.remove(group.id)
                    }
                }
                //开启R18搜索条件
                always {
                    if (Command.command_R18on.contains(message.contentToString())) {
                        if (!Mydata.groups.contains(group.id)) {
                            group.sendMessage("此群没有色图权限")
                            return@always
                        }
                        group.sendMessage("R18限制已解除")
                        Mydata.R18.add(group.id)
                    }
                }
            }

            //封印解除
            always {
                if (Command.command_on.contains(message.contentToString())) {
                    if (checkpower(sender)) {
                        group.sendMessage("启用该群色图功能")
                        if (!Mydata.groups.contains(group.id))
                            Mydata.groups.add(group.id)
                    } else
                        group.sendMessage("你不是我的主人，我不能听从你的命令")
                }
            }
            //封印
            always {
                if (Command.command_off.contains(message.contentToString())) {
                    if (checkpower(sender)) {
                        group.sendMessage("已禁用该群色图功能")
                        Mydata.groups.remove(group.id)
                    } else
                        group.sendMessage("你不是我的主人，我不能听从你的命令")
                }
            }
        }
    }

}

private fun String.startsWith(list: MutableList<String>): Boolean {
    list.forEach {
        if(this.startsWith(it))
            return true
    }
    return false
}

//布尔型转short星自定义
private fun Boolean.toShort(): Short {
    return if (this) 1 else 0
}
