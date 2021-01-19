package com.blrabbit.mirai.setu

import com.blrabbit.mirai.MiraiSetuMain
import com.blrabbit.mirai.Util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages

fun SetuEntrance(){
    GlobalEventChannel.subscribeGroupMessages {
        always() {
            if (Command.command_get.contains(message.contentToString())) {
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
                    MiraiSetuMain.logger.error(e)
                }
            }
        }
        //搜色图
        always {
            if (message.contentToString().startsWith(Command.command_search)) {
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
                    MiraiSetuMain.logger.error(e)
                }
            }
        }
        //开启R18模式增加的模式
        if (MySetting.R18) {
            //关闭R18搜索条件
            always {
                if (Command.command_R18off.contains(message.contentToString())) {
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
