package cn.blrabbit.mirai.Util.storge

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MySetting : ReadOnlyPluginConfig("setu-config") {
    @ValueDescription("设置此插件主人的id。")
    val masterid by value(mutableListOf<Long>(123456))
    @ValueDescription("设置lolicon的APIKEY，可以不设置，但是每天调用次数会比较少。")
    val APIKEY by value("365007185fc06c84ac62e6")

    @ValueDescription("代理设置,0为不使用代理，1为使用http代理，2为使用socks代理")
    val proxyconfig by value(0)
    val httpproxy by value(HttpProxy())
    val socksproxy by value(SocksProxy())

    @Serializable
    data class SocksProxy(
        val host: String = "127.0.0.1",
        val port: Int = 4001
    )

    @Serializable
    data class HttpProxy(
        val proxy: String = "http://127.0.0.1:80"
    )

    @ValueDescription("反向代理的域名，修改为i.pximg.net可以直连")
    val domainproxy by value("i.pixiv.cat")
}
