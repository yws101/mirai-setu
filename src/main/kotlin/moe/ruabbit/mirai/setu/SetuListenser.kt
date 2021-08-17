package moe.ruabbit.mirai.setu

import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import moe.ruabbit.mirai.PluginMain.checkPermission
import moe.ruabbit.mirai.PluginMain.logger
import moe.ruabbit.mirai.config.CommandConfig
import moe.ruabbit.mirai.config.MessageConfig
import moe.ruabbit.mirai.data.SetuData
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages

/**
 * 色图功能的入口函数
 * @author bloody-rabbit
 * @version 1.3 2021/8/3
 */
@InternalAPI
@ExperimentalSerializationApi
fun setuListenerRegister() {
    GlobalEventChannel.subscribeGroupMessages {
        /**
         * 获取色图
         */
        CommandConfig.get.forEach { getcommand ->
            startsWith(getcommand) {
                try {
                    val setu: Setu = Loliconv2Requester(subject)
                    if (it.isBlank())
                        setu.requestSetu(2, SetuData.groupPolicy[subject.id]!!)
                    else {
                        setu.requestSetu(it.toInt(), SetuData.groupPolicy[subject.id]!!)
                    }
                    setu.sendmessage()
                } catch (e: NumberFormatException) {
                    group.sendMessage("获取的数量参数错误，请输入纯数字")
                } catch (e: Exception) {
                    logger.error(e)
                }
            }
        }
        /**
         * 搜索色图
         */
        CommandConfig.search.forEach { searchcommand ->
            startsWith(searchcommand) {
                try {
                    val setu: Setu = Loliconv2Requester(subject)
                    val tags = it.split(Regex("\\s+"))
                    if (tags.isEmpty()) {
                        group.sendMessage("请输入要搜索的关键词")
                    } else {
                        setu.requestSetu(tags, 2, SetuData.groupPolicy[subject.id]!!)
                    }
                    setu.sendmessage()
                } catch (e: NumberFormatException) {
                    group.sendMessage("获取的数量参数错误，请输入纯数字")
                } catch (e: Exception) {
                    logger.error(e)
                }
            }
        }
        /**
         * 关闭色图插件
         */
        CommandConfig.off.forEach {
            case(it) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == null) {
                        group.sendMessage(MessageConfig.setuOffAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuOff)
                        SetuData.groupPolicy.remove(group.id)
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
        }
        /**
         * 设置为普通模式
         */
        CommandConfig.setSafeMode.forEach {
            case(it) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 0) {
                        group.sendMessage(MessageConfig.setuSafeAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuSafe)
                        SetuData.groupPolicy[group.id] = 0
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }

        }
        /**
         * 设置为R-18模式
         */
        CommandConfig.setNsfwMode.forEach {
            case(it) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 1) {
                        group.sendMessage(MessageConfig.setuNsfwAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuNsfw)
                        SetuData.groupPolicy[group.id] = 1
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
        }
        /**
         * 设置为混合模式
         */
        CommandConfig.setBothMode.forEach {
            case(it) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 2) {
                        group.sendMessage(MessageConfig.setuBothAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuBoth)
                        SetuData.groupPolicy[group.id] = 2
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
        }
    }
}