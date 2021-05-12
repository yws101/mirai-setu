package cn.blrabbit.mirai.setu

import cn.blrabbit.mirai.KtorUtils
import cn.blrabbit.mirai.PluginMain
import cn.blrabbit.mirai.config.MessageConfig
import cn.blrabbit.mirai.config.SettingsConfig
import cn.blrabbit.mirai.data.SetuData
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.utils.error
import java.io.InputStream


//直连pixiv的示例
/*
@KtorExperimentalAPI
suspend fun getPixiv(): InputStream {
    return client.get("https://i.pximg.net/img-original/img/2021/01/17/00/30/01/87098740_p0.jpg") {
        headers.append("referer", "https://www.pixiv.net/")
    }
}*/
class LoliconRequester(private val subject: Group) {
    // 图片数据
    private lateinit var imageResponse: LoliconResponse.SetuImageInfo
    private var largeUrl: String = ""

    @KtorExperimentalAPI
    suspend fun requestSetu() {
        try {
            val response: String =
                KtorUtils.proxyClient.get("http://api.lolicon.app/setu?apikey=${SettingsConfig.loliconApiKey}&r18=${SetuData.groupPolicy[subject.id]}")
            parseSetu(response)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(SettingsConfig.loliconApiKey, "/$/{APIKEY/}"))
            PluginMain.logger.error(e)
            throw e
        }
    }

    @KtorExperimentalAPI
    suspend fun requestSetu(keyword: String) {
        try {
            val setuResponse: String =
                KtorUtils.proxyClient.get("http://api.lolicon.app/setu?apikey=${SettingsConfig.loliconApiKey}&keyword=${keyword}&r18=${SetuData.groupPolicy[subject.id]}\"")
            parseSetu(setuResponse)
        } catch (e: Exception) {
            subject.sendMessage("出现错误\n" + e.message?.replace(SettingsConfig.loliconApiKey, "/$/{APIKEY/}"))
            PluginMain.logger.error(e)
            throw e
        }
    }

    private fun parseSetu(rawJson: String) {
        val loliconResponse: LoliconResponse = Json.decodeFromString(rawJson)
        fun parseErrCode(message: String): String {
            return message
                .replace("%code%", loliconResponse.code.toString())
                .replace("%msg%", loliconResponse.msg)
        }

        when (loliconResponse.code) {
            0 -> {
                SetuData.quota = loliconResponse.quota
                loliconResponse.data?.get(0)?.let {
                    PluginMain.logger.info("剩余调用次数 ${loliconResponse.quota}")
                    //拼装成缩略图URL
                    imageResponse = it
                    largeUrl = imageResponse.url
                        .replace("img-original", "c/600x1200_90_webp/img-master")
                        .replace(".png", ".jpg")
                        .replace(".jpg", "_master1200.jpg")
                }
            }
            // API错误
            401 -> {
                PluginMain.logger.error { "lolicon 错误代码：${loliconResponse.code} 错误信息：${loliconResponse.msg}" }
                throw Exception(parseErrCode(MessageConfig.setuFailureCode401))
            }
            // 色图搜索404
            404 -> {
                PluginMain.logger.error { "lolicon 错误代码：${loliconResponse.code} 错误信息：${loliconResponse.msg}" }
                throw Exception(parseErrCode(MessageConfig.setuFailureCode404))
            }
            // 调用到达上限
            429 -> {
                PluginMain.logger.error { "lolicon 错误代码：${loliconResponse.code} 错误信息：${loliconResponse.msg}" }
                throw Exception(parseErrCode(MessageConfig.setuFailureCode429))
            }
            // -1和403 错误等一系列未知错误
            else -> {
                PluginMain.logger.error { "发生此错误请到github反馈错误 lolicon错误代码：${loliconResponse.code} 错误信息：${loliconResponse.msg}" }
                throw Exception(parseErrCode(MessageConfig.setuFailureCodeElse))
            }
        }
    }

    // 解析字符串
    private fun parseMessage(message: String): String {
        return message
            .replace("%pid%", imageResponse.pid.toString())
            .replace("%p%", imageResponse.p.toString())
            .replace("%uid%", imageResponse.uid.toString())
            .replace("%title%", imageResponse.title)
            .replace("%author%", imageResponse.author)
            .replace("%original_url%", imageResponse.url)
            .replace("%r18%", imageResponse.r18.toString())
            .replace("%width%", imageResponse.width.toString())
            .replace("%height%", imageResponse.height.toString())
            .replace("%tags%", imageResponse.tags.toString())
            .replace("%large_url", largeUrl)
    }

    @KtorExperimentalAPI
    suspend fun getOriginalImage(): InputStream {
        return KtorUtils.proxyClient.get(imageResponse.url.replace("i.pixiv.cat", SettingsConfig.domainProxy)) {
            // todo 研究研究为什么会出现上传电脑不显示的问题
            headers.append("referer", "https://www.pixiv.net/")
        }
    }

    @KtorExperimentalAPI
    suspend fun getLargeImage(): InputStream {
        return KtorUtils.proxyClient.get(largeUrl.replace("i.pixiv.cat", SettingsConfig.domainProxy)) {
            // todo 增加本地缓存，读取本地的缓存文件减少重复获取
            headers.append("referer", "https://www.pixiv.net/")
        }
    }


    @KtorExperimentalAPI
    suspend fun sendSetu() {
        // 发送信息
        val setuInfoMsg = subject.sendMessage(parseMessage(MessageConfig.setuReply))
        var setuImageMsg: MessageReceipt<Group>? = null
        // 发送setu
        if (SettingsConfig.useOriginalImage) {
            try {
                setuImageMsg = subject.sendImage(getOriginalImage())
                // todo 捕获群上传失败的错误信息返回发送失败的信息（涩图被腾讯拦截）
            } catch (e: ClientRequestException) {
                subject.sendMessage(MessageConfig.setuImage404)
            } catch (e: Exception) {
                // 隐藏apikey发送到群里去
                subject.sendMessage("出现错误" + e.message?.replace(SettingsConfig.loliconApiKey, "/$/{APIKEY/}"))
                PluginMain.logger.error(e)
                throw e
            }
        } else {
            try {
                setuImageMsg = subject.sendImage(getLargeImage())
            } catch (e: ClientRequestException) {
                try {
                    setuImageMsg = subject.sendImage(getOriginalImage())
                } catch (e: ClientRequestException) {
                    subject.sendMessage(MessageConfig.setuImage404)
                }
            } catch (e: Exception) {
                subject.sendMessage("出现错误" + e.message?.replace(SettingsConfig.loliconApiKey, "/$/{APIKEY/}"))
                PluginMain.logger.error(e)
                throw e
            }
        }
        // 撤回图片
        if (SettingsConfig.autoRecallTime > 0) {
            try {
                setuImageMsg?.recallIn(millis = SettingsConfig.autoRecallTime)
            } catch (e: Exception) {
            }
            try {
                setuInfoMsg.recallIn(millis = SettingsConfig.autoRecallTime)
            } catch (e: Exception) {
            }
        }
    }

}
