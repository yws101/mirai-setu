package cn.blrabbit.mirai.saucenao.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class SaucenaoJson(
    // 返回值判断查询是否成功
    val header: Header,
    val result: List<Result>

) {
    @Serializable
    data class Header(
        val user_id: String,
    )

    @Serializable
    data class Result(
        val header: Header,
        val data: Data
    ) {
        @Serializable
        data class Header(
            val similarity: String,
            val thumbnail: String
        )

        @Serializable
        data class Data(
            val ext_urls: List<String>, // 原链接地址
            val title: String // 标题
        )
    }

}
