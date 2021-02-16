package org.example.mirai.plugin.JsonData

import kotlinx.serialization.Serializable

@Serializable
data class LoliconJson(
    val code: Int,
    val msg: String = "",
    val quota: Int,
    val quota_min_ttl: Int,
    val count: Int,
    val data: List<SetuImageJson>? = null
)
