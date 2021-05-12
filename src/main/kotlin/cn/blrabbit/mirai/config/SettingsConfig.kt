package cn.blrabbit.mirai.config

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object SettingsConfig : ReadOnlyPluginConfig("Settings") {
    @ValueDescription(
        """
            插件权限控制设置
            0 为所有人都可以控制
            1 为只有插件主人可以进行配置
            2 为群管理员也可以配置
            3 为拥有权限（setu:admin）者可以配置
            """
    )
    val permitMode by value(1)

    @ValueDescription("设置此插件主人的id。")
    val masterId by value(mutableListOf<Long>(123456))

    @ValueDescription("设置lolicon的APIKEY，可以不设置，但是每天调用次数会比较少。(https://api.lolicon.app/#/setu)可以获取自己的api来获取稳定的涩图请求量")
    val loliconApiKey by value("365007185fc06c84ac62e6")

    @ValueDescription("设置SauceNAO的APIKEY，可以不设置，如果大量使用请自行申请APIkey避免遇到403问题")
    val sauceNaoApiKey by value("5fe827fb6ef3284d73a031760cb2f7185ce1b380")

    @ValueDescription(
        """
            设置SauceNAO的数据库
            5为只搜索pixiv数据库
            """
    )
    val sauceNaoDataBaseMode by value(5)

    @ValueDescription(
        """
            代理设置,0为不使用代理，1为使用http代理，2为使用socks代理
            代理只对色图的获取生效
            """
    )
    val proxyConfig by value(0)
    val httpProxy by value(HttpProxy())
    val socksProxy by value(SocksProxy())

    @Serializable
    data class SocksProxy(
        val host: String = "127.0.0.1",
        val port: Int = 4001
    )

    @Serializable
    data class HttpProxy(
        val proxy: String = "http://127.0.0.1:80"
    )

    @ValueDescription(
        """
            反向代理的域名，修改为i.pximg.net可以直连
            使用直连提高访问稳定性，但是需要科学上网
            """
    )
    val domainProxy by value("i.pixiv.cat")

    @ValueDescription(
        """
            是否使用原图发送。
            已知bug，不使用原图发送可以提高发送速度，但是在pc上无法显示图片，手机上无法直接转发
            """
    )
    val useOriginalImage by value(false)

    @ValueDescription(
        """
            设置涩图的自动撤回时间
            单位毫秒,-1为不撤回
            """
    )
    val autoRecallTime: Long by value(-1L)
}
