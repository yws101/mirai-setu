package moe.ruabbit.mirai.setu

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.ruabbit.mirai.KtorUtils.normalClient
import moe.ruabbit.mirai.data.SetuData
import net.mamoe.mirai.contact.Contact

/**
 * LoliconV2的相关方法
 * @author bloodt-rabbit
 * @version 1.3 2021/8/5
 * @param subject 传入的消息来自对象，供发送和上传图片使用
 */
@ExperimentalSerializationApi
class Loliconv2Requester(val subject: Contact) : Setu(subject) {
    lateinit var setudata: List<Loliconv2Response.Data>

    /**
     *  构造post请求的body信息
     *  @param r18 是否获取R18模式(1.普通模式 2.R18模式 3.混合模式)
     *  @param num 获取图片的数量
     *  @param keyword 关键词搜索，单个词搜索题目，作者，以及tag
     *  @param tag 多tag搜索的多个tag
     */
    @Serializable
    data class Loliconrequest(
        val r18: Int,
        val num: Int,
        val keyword: String? = null,
        val tag: List<String>? = null
    )

    /**
     * 从Lolicon请求获取随机色图
     * @param num 请求的色图数量
     */
    override suspend fun requestSetu(num: Int) {
        // TODO("设置请求的数量上限")
        // 构造post请求的body
        val requsterbody = Loliconrequest(SetuData.groupPolicy[subject.id]!!, num)

        val response: HttpResponse = normalClient.post("https://api.lolicon.app/setu/v2") {
            body = TextContent(Json.encodeToString(requsterbody), ContentType.Application.Json)
        }

        when (response.status.value) {
            200 -> {
                val str = response.content.readUTF8Line()!!
                val json = Json.decodeFromString<Loliconv2Response>(str)
                setudata = json.data
            }
            else -> {
                throw RemoteApiException("")
            }
        }

    }

    /**
     * 从Lolilcon请求获取包含tag的色图
     * @param tags 获取的关键词
     * @param num 请求的色图数量
     */
    override suspend fun requestSetu(tags: List<String>, num: Int) {
        val requsterbody: Loliconrequest =
            if (tags.size == 1)
                Loliconrequest(SetuData.groupPolicy[subject.id]!!, num, keyword = tags[0])
            else
                Loliconrequest(SetuData.groupPolicy[subject.id]!!, num, tag = tags)

        val response: HttpResponse = normalClient.post("https://api.lolicon.app/setu/v2") {
            body = TextContent(Json.encodeToString(requsterbody), ContentType.Application.Json)
        }
    }

    /**
     * 发送色图的信息到[subject]
     */
    override suspend fun sendmessage() {
        setudata.forEach {
            subject.sendMessage(it.toString())
        }
    }
}