package com.blrabbit.mirai.setu

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.code.parseMiraiCode
import net.mamoe.mirai.message.sendImage
import net.mamoe.mirai.utils.error
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning
import okhttp3.OkHttpClient
import java.io.InputStream
import java.net.SocketTimeoutException

val client = OkHttpClient()

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.blrabbit.mirai-setu",
        version = "0.1.4"
    )
) {
    override fun onEnable() {
        MySetting.reload()//初始化配置数据
        Mydata.reload()//初始化插件数据
        logger.info { "提示：${MySetting.name}加载完成" }

        if (MySetting.APIKEY.isEmpty()) {
            logger.warning { "未设置lolicon的APIKEY，可能会遇到调用上限的问题" }
            logger.warning { "申请地址: https://api.lolicon.app/#/setu" }
            logger.warning { "请到 config/com.blrabbit.mirai-setu/setu-config.yml 添加APIKEY" }
        }
        if (MySetting.masterid.equals(0)) {
            logger.error { "未设置主人ID，插件可能会无法使用" }
            logger.error { "请到 config/com.blrabbit.mirai-setu/setu-config.yml 添加masterid" }
        }
        subscribeGroupMessages {

            case("昨日番剧") {
                reply(GetBiliTimeline("昨日番剧",5))
            }

            case("今日番剧") {
                reply(GetBiliTimeline("今日番剧", 6))
            }

            case("明日番剧") {
                reply(GetBiliTimeline("明日番剧", 7))
            }


            //获取色图的触发词
            case(MySetting.command_get) {
                if (!Mydata.groups.contains(group.id)) {
                    reply("对不起暂时此群没有权限，请联系主人启用")
                    return@case
                }
                if (Mydata.R18.contains(group.id)) {
                    group.sendMessage("正在获取图片中，请稍后\n[R18模式已启用]")
                } else
                    group.sendMessage("正在获取图片，请稍后")
                //json解析到result
                try {
                    val result = Klaxon().parse<Image>(Getsetu(Mydata.R18.contains(group.id).toShort()))
                    if (result != null) {
                        if (result.code == 0) {
                            Mydata.quota = result.quota
                            //向群内发送信息
                            sender.group.sendMessage("pid：${result.data[0].pid}\n" +
                                "title: ${result.data[0].title}\n" +
                                "author: ${result.data[0].author}\n" +
                                "url: ${result.data[0].url}\n" +
                                "tags: ${result.data[0].tags}")
                            logger.info("剩余调用次数 ${result.quota}")

                            val a: InputStream? = Downsetu(result.data[0].url)
                            //发送图片，可能会被腾讯吞掉
                            if (a != null) {
                                group.sendImage(a)
                            }
                        } else {
                            reply("超出调用次数")
                        }
                    }
                } catch (e: SocketTimeoutException) {
                    reply(("[mirai:at:${sender.id},@${sender.nameCard}]").parseMiraiCode() + " 出现了意外的错误\n$e")
                }
            }
            //启用R18模式
            case(MySetting.command_R18on) {
                if (!Mydata.groups.contains(group.id)) {
                    reply("对不起暂时此群没有权限，请联系的主人启用")
                    return@case
                }
                reply("警告，R18限制已解除")
                Mydata.R18.add(group.id)
            }
            //关闭R18模式
            case(MySetting.command_R18off) {
                if (!Mydata.groups.contains(group.id)) {
                    reply("对不起暂时此群没有权限，请联系的主人启用")
                    return@case
                }
                reply("R18已关闭")
                Mydata.R18.remove(group.id)
            }

            startsWith(MySetting.command_search) {

                if (!Mydata.groups.contains(group.id)) {
                    reply("对不起暂时此群没有权限，请联系的主人启用")
                    return@startsWith
                }

                sender.group.sendMessage("正在获取图片，请稍后")
                //json解析到result
                val result = Klaxon()
                    .parse<Image>(Getsetu(Mydata.R18.contains(group.id).toShort(), it))
                if (result != null) {
                    if (result.code == 0) {
                        Mydata.quota = result.quota
                        //向群内发送信息
                        sender.group.sendMessage("pid：${result.data[0].pid}\n" +
                            "title: ${result.data[0].title}\n" +
                            "author: ${result.data[0].author}\n" +
                            "url: ${result.data[0].url}\n" +
                            "tags: ${result.data[0].tags}")
                        logger.info("剩余调用次数 ${result.quota}")

                        val a: InputStream? = Downsetu(result.data[0].url)
                        if (a != null) {
                            group.sendImage(a)
                        }
                    } else if (result.code == 404) {
                        reply("没有符合条件的色图desu~")
                    } else {
                        reply("超出调用次数")
                    }
                }
            }

            case("setu状态检查") {
                sender.group.sendMessage("剩余调用次数：${Mydata.quota}\n" +
                    "当前R18模式 ${Mydata.R18.contains(group.id)}\n" +
                    "此群是否开启此插件${Mydata.groups.contains(group.id)}")
            }

            case("封印解除") {
                if (sender.id == MySetting.masterid) {
                    reply("启用该群色图功能")
                    if (!Mydata.groups.contains(group.id))
                        Mydata.groups.add(group.id)
                } else
                    reply("你不是我的主人，我不能听从你的命令")
            }

            case("封印") {
                reply("已禁用该群色图功能")
                Mydata.groups.remove(group.id)
            }

        }
    }
}

private fun Boolean.toShort(): Short {
    return if (this) 1 else 0
}

//配置文件存储
object MySetting : AutoSavePluginConfig("setu-config") {
    val name by value("setu")
    val masterid: Long by value()
    val APIKEY by value("")
    val command_get by value("色图时间")
    val command_R18off by value("青少年模式")
    val command_R18on by value("青壮年模式")
    val command_search by value("搜色图")
}

//配置数据存储
object Mydata : AutoSavePluginData("setu-data") {
    var quota by value(-1)
    var R18: MutableList<Long> by value()
    var groups: MutableList<Long> by value()

}
