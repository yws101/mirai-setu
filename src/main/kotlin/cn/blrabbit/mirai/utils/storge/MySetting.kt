package cn.blrabbit.mirai.utils.storge

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MySetting : ReadOnlyPluginConfig("setu-config") {
    @ValueDescription("插件权限控制设置\n1为只有插件主人可以进行配置，2为群管理员也可以配置")
    val mastermode by value(1)

    @ValueDescription("设置此插件主人的id。")
    val masterid by value(mutableListOf<Long>(123456))

    @ValueDescription("设置lolicon的APIKEY，可以不设置，但是每天调用次数会比较少。(https://api.lolicon.app/#/setu)可以获取自己的api来获取稳定的涩图请求量")
    val LoliconAPIKEY by value("365007185fc06c84ac62e6")

    @ValueDescription("设置SauceNAO的APIKEY，可以不设置，如果大量使用请自行申请APIkey避免遇到403问题")
    val SauceNAOAPIKEY by value("5fe827fb6ef3284d73a031760cb2f7185ce1b380")

    @ValueDescription("设置SauceNAO的数据库\n5为只搜索pixiv数据库")
    val SauceNAODB by value(5)

    @ValueDescription("代理设置,0为不使用代理，1为使用http代理，2为使用socks代理\n 代理只对色图的获取生效")
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

    @ValueDescription("反向代理的域名，修改为i.pximg.net可以直连\n使用直连提高访问稳定性，但是需要科学上网")
    val domainproxy by value("i.pixiv.cat")

    @ValueDescription("是否使用原图发送。\n已知bug，不使用原图发送可以提高发送速度，但是在pc上无法显示图片，手机上无法直接转发")
    val useoriginalImage by value(false)

    @ValueDescription("设置涩图的自动撤回时间\n单位毫秒,-1为不撤回")
    val seturecall by value(-1)
}
