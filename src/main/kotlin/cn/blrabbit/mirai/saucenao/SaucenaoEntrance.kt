package cn.blrabbit.mirai

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.nextMessage

fun SaucenaoEntrace() {
    GlobalEventChannel.subscribeMessages {
        always {
            if (message.contentToString().startsWith("以图搜图")) {
                val image = message[Image]
                if (image == null) {
                    subject.sendMessage("没有图片的说,请在10s内发送图片")
                    nextMessage(timeoutMillis = 1000) // 挂起协程1000毫秒
                        .get(Image).let {
                            subject.sendMessage(it!!.queryUrl())
                        }
                    subject.sendMessage("协程释放")
                } else {
                    subject.sendMessage(image.queryUrl())
                }
            }
        }
    }
}