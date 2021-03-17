package cn.blrabbit.mirai

import cn.blrabbit.mirai.saucenao.Saucenao
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.nextMessage

fun SaucenaoEntrace() {
    GlobalEventChannel.subscribeMessages {
        always {
            if (message.contentToString().startsWith("以图搜图")) {
                val saucenao = Saucenao(subject)
                val image = message[Image]
                if (image == null) {
                    subject.sendMessage("没有图片的说,请在10s内发送图片")
                    val next = nextMessage()[Image]
                    if (next == null) {
                        subject.sendMessage("没有获取图片")
                    } else {
                        saucenao.search(next)
                        saucenao.sendmessage()
                    }
                } else {
                    saucenao.search(image)
                    saucenao.sendmessage()
                }
            }
        }
    }
}