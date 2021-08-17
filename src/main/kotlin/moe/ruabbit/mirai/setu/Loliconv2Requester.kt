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
     *  @param r18 0为非 R18，1为 R18，2为混合（在库中的分类，不等同于作品本身的 R18 标识）
     *  @param num 一次返回的结果数量，范围为1到100；在指定关键字或标签的情况下，结果数量可能会不足指定的数量
     *  @param uid 返回指定uid作者的作品，最多20个
     *  @param keyword 返回从标题、作者、标签中按指定关键字模糊匹配的结果，大小写不敏感，性能和准度较差且功能单一，建议使用tag代替
     *  @param tag 返回匹配指定标签的作品
     *  @param size 返回指定图片规格的地址
     *  @param proxy 设置图片地址所使用的在线反代服务
     *  @param dateAfter 返回在这个时间及以后上传的作品；时间戳，单位为毫秒
     *  @param dateBefore 返回在这个时间及以前上传的作品；时间戳，单位为毫秒
     *  @param dsc 设置为任意真值以禁用对某些缩写keyword和tag的自动转换
     */
    @Serializable
    data class LoliconRequest(
        val r18: Int = 0,
        val num: Int = 1,
        val uid: Int? = null,
        val keyword: String? = null,
        val tag: List<String>? = null,
        val size: List<String> = listOf("original"),
        val proxy: String = "i.pixiv.cat",
        val dateAfter: Int? = null,
        val dateBefore: Int? = null,
        val dsc: Boolean = false
    )

    /**
     * 从Lolicon请求获取随机色图
     * @param num 请求的色图数量
     * @param r18 0普通图，1r18图，2随机返回
     */
    override suspend fun requestSetu(num: Int, r18: Int) {
        // TODO("设置请求的数量上限")
        // 构造post请求的body
        val requsterbody = LoliconRequest(r18 = r18, num = num)

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
                LoliconRequest(r18 = r18, num = num, keyword = tags[0])
            else
                LoliconRequest(r18 = r18, num = num, tag = tags)

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