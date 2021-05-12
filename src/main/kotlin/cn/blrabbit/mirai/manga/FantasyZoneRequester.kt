package cn.blrabbit.mirai.manga

import cn.blrabbit.mirai.KtorUtils
import cn.blrabbit.mirai.PluginMain
import io.ktor.client.request.*
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.InputStream

class FantasyZoneRequester(private val subject: Contact) {

    suspend fun sendNormal() {
        try {
            val image = KtorUtils.normalClient.get<InputStream>("https://www.fantasyzone.cc/api/tu") {
                headers.append(
                    "user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.68"
                )
            }.uploadAsImage(subject)
            subject.sendMessage(image)
        } catch (e: Exception) {
            PluginMain.logger.error(e)
        }
    }
}
