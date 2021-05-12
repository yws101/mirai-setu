package cn.blrabbit.mirai.manga

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages

fun fantasyZoneRegister() {
    GlobalEventChannel.subscribeMessages {
        case("动漫图片") {
            val normal = FantasyZoneRequester(subject)
            normal.sendNormal()
        }
    }
}
