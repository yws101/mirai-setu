package cn.blrabbit.mirai.bilibili

import cn.blrabbit.mirai.MiraiSetuMain
import cn.blrabbit.mirai.Util.startsWith
import cn.blrabbit.mirai.Util.storge.Command
import com.blrabbit.mirai.setu.GetbangumiTimeline
import io.ktor.util.*
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl

@KtorExperimentalAPI
fun BiliBiliEntrace() {
    GlobalEventChannel.subscribeMessages {
        //昨日番剧
        always {
            if (Command.bilibilicommand.command_bangumiyesterday.contains(message.contentToString())) {
                try {
                    MiraiSetuMain.logger.info("请求昨日番剧")
                    subject.sendMessage(GetbangumiTimeline(message.contentToString(), 5))
                } catch (e: Exception) {
                    subject.sendMessage("出现异常，请到控制台查看详细错误信息\n${e.message}")
                    MiraiSetuMain.logger.error(e)
                }
            }
        }
        //今日番剧
        always {
            if (Command.bilibilicommand.command_bangumitoday.contains(message.contentToString())) {
                try {

                    subject.sendMessage(GetbangumiTimeline(message.contentToString(), 6))
                } catch (e: Exception) {
                    subject.sendMessage("出现异常，请到控制台查看详细错误信息\n${e.message}")
                    MiraiSetuMain.logger.error(e)
                }
            }
        }
        //明日番剧
        always {
            if (Command.bilibilicommand.command_bangumitomorrow.contains(message.contentToString())) {
                try {
                    MiraiSetuMain.logger.info("请求明日番剧")
                    subject.sendMessage(GetbangumiTimeline(message.contentToString(), 7))
                } catch (e: Exception) {
                    subject.sendMessage("出现异常，请到控制台查看详细错误信息\n${e.message}")
                    MiraiSetuMain.logger.error(e)
                }
            }
        }
        //番剧搜索
        always {
            if (message.contentToString().startsWith(Command.bilibilicommand.command_searchbangumibyimage)) {
                if (it.isEmpty())
                    subject.sendMessage("搜索信息空")
                subject.sendMessage("测试功能，可能存在不稳定")
                try {
                    val a = message[Image]
                    if (a != null) {
                        MiraiSetuMain.logger.info(a.queryUrl())
                        serchmoe(a.queryUrl(), subject as Group)
                    }
                } catch (e: Exception) {
                    subject.sendMessage(e.toString())
                }
            }
        }

    }
}