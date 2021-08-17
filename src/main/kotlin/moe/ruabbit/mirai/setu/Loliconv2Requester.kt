package moe.ruabbit.mirai.setu

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.ruabbit.mirai.KtorUtils.normalClient
import moe.ruabbit.mirai.config.SettingsConfig
import moe.ruabbit.mirai.data.SetuData
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import java.io.InputStream

/**
 * LoliconV2的相关方法
 * @author bloodt-rabbit
 * @version 1.3 2021/8/5
 * @param subject 传入的消息来自对象，供发送和上传图片使用
 */
@InternalAPI
@ExperimentalSerializationApi
class Loliconv2Requester(val subject: Contact) : Setu(subject) {
    lateinit var setudata: List<Loliconv2Response.Data>

    /**
     *  构造post请求的body信息，注意这个body并不是完整的body，详细的请求信息请参考官网的配置信息
     *  @param r18 是否获取R18模式(0.普通模式 1.R18模式 2.混合模式)
     *  @param num 获取图片的数量
     *  @param keyword 关键词搜索，单个词搜索题目，作者，以及tag
     *  @param tag 多tag搜索的多个tag
     */
    @Serializable
    data class LoliconRequest(
        val r18: Int,
        val num: Int,
        val keyword: String? = null,
        val tag: List<String>? = null
    )

    /**
     * 从Lolicon请求获取随机色图
     * @param num 请求的色图数量
     * @param r18 0普通图，1r18图，2随机返回
     */
    override suspend fun requestSetu(num: Int, r18: Int) {
        // TODO("设置请求的数量上限")
        // 构造post请求的body
        val requsterbody = LoliconRequest(SetuData.groupPolicy[subject.id]!!, num)

        parseresponse(requsterbody)
    }

    /**
     * 从Lolilcon请求获取包含tag的色图
     * @param tags 获取的关键词
     * @param num 请求的色图数量
     * @param r18 0普通图，1r18图，2随机返回
     */
    override suspend fun requestSetu(tags: List<String>, num: Int, r18: Int) {
        val requsterbody: LoliconRequest =
            if (tags.size == 1)
                LoliconRequest(r18, num, keyword = tags[0])
            else
                LoliconRequest(r18, num, tag = tags)

        parseresponse(requsterbody)

        if (setudata.isEmpty()) {
            subject.sendMessage("找到了${setudata.size}张图片")
        }
    }

    /**
     * 发送色图的信息到[subject]
     */
    override suspend fun sendmessage(): Unit = coroutineScope {
        setudata.forEach {
            launch {
                try {
                    val imagestram: InputStream = normalClient.get {
                        url(it.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy))
                        headers {
                            // 直接访问pixiv添加Referrer会导致403
                            append(HttpHeaders.Referrer, "https://www.pixiv.net/")
                        }
                    }
                    subject.sendImage(imagestram)
                } catch (e: Exception) {
                    subject.sendMessage(e.toString())
                }
            }
        }
    }

    /**
     * 解析返回的数据并赋值到[setudata]
     * @param requsterbody post的body[LoliconRequest]
     * @throws Exception
     */
    private suspend fun parseresponse(requsterbody: LoliconRequest) {
        val response: HttpResponse = normalClient.post {
            url("https://api.lolicon.app/setu/v2")
            body = TextContent(Json.encodeToString(requsterbody), ContentType.Application.Json)
        }

        when (response.status.value) {
            200 ->
                setudata = Json.decodeFromString<Loliconv2Response>(response.content.readUTF8Line()!!).data
            else -> {
                throw Exception()
            }
        }
    }

}