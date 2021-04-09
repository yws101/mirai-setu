package cn.blrabbit.mirai.fantasyzone

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages

fun FantasyzoneEntrace() {
    GlobalEventChannel.subscribeMessages {
        case("动漫图片") {
            val normal = Fantasyzone(subject)
            normal.sendnormal()
        }
    }
}
