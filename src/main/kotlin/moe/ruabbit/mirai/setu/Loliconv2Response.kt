package moe.ruabbit.mirai.setu

import kotlinx.serialization.Serializable

/**
 * LoliconV2返回的数据格式，详细查阅 [LoliconApi](https://api.lolicon.app/setu/v2)
 * @author bloody-rabbit
 * @version 1.3 2021/8/2
 */
@Serializable
data class Loliconv2Response(
    val data: List<Data>,
    val error: String,
) {
    @Serializable
    data class Data(
        val author: String,
        val ext: String,
        val height: Int,
        val p: Int,
        val pid: Int,
        val r18: Boolean,
        val tags: List<String>,
        val title: String,
        val uid: Int,
        val uploadDate: Long,
        val urls: Urls,
        val width: Int
    )

    @Serializable
    data class Urls(
        val original: String
    )
}