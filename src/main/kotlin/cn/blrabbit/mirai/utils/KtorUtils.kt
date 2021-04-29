package cn.blrabbit.mirai.utils

import cn.blrabbit.mirai.utils.storge.MySetting
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.util.*

class KtorUtils {
    companion object {
        // 使用代理的ktor客户端
        @OptIn(KtorExperimentalAPI::class)
        val proxyclient = HttpClient(OkHttp) {
            engine {
                proxy = when (MySetting.proxyconfig) {
                    0 -> null
                    1 -> ProxyBuilder.http(MySetting.httpproxy.proxy)
                    2 -> ProxyBuilder.socks(host = MySetting.socksproxy.host, port = MySetting.socksproxy.port)
                    else -> null
                }
            }
        }

        // 未使用代理的Ktor客户端
        val normalclient = HttpClient(OkHttp)

        // 安全的关闭客户端，防止堵塞主线程
        fun closeClient() {
            proxyclient.close()
            normalclient.close()
        }
    }
}
