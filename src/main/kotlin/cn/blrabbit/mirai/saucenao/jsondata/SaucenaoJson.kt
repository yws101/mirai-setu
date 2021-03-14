package cn.blrabbit.mirai.saucenao.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class SaucenaoJson(
    // 返回值判断查询是否成功
    val header: Header

) {
    @Serializable
    data class Header(
        val user_id: String,
    )
}
