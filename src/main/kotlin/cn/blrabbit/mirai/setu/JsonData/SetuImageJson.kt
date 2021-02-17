package org.example.mirai.plugin.JsonData

import kotlinx.serialization.Serializable

//色图json数据
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
