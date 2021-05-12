package cn.blrabbit.mirai.setu

import kotlinx.serialization.Serializable

@Serializable
data class FantasyZoneResponse(
    val id: String?,
    val description: String?,
    val userId: String?,
    val userName: String?,
    val title: String?,
    val tags: List<String>?,
    val width: String?,
    val height: String?,
    val url: String
)
