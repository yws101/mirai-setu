package cn.blrabbit.mirai.lolicon.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class LoliconJson(
    val code: Int,
    val msg: String = "",
    val quota: Int,
    val quota_min_ttl: Int,
    val count: Int,
    val data: List<SetuImageJson>? = null
) {
    @Serializable
    data class SetuImageJson(
        val pid: Int,
        val p: Int,
        val uid: Int,
        val title: String,
        val author: String,
        val url: String,
        val r18: Boolean,
        val width: Int,
        val height: Int,
        val tags: List<String>
    )
}
