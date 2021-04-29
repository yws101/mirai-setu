package cn.blrabbit.mirai.fantasyzone.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class FantasyZoneJson(
    val description: String,
    val height: String,
    val id: String,
    val tags: List<String>,
    val title: String,
    val url: String,
    val userId: String,
    val userName: String,
    val width: String
)
