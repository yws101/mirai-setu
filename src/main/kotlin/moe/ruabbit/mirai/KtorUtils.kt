package moe.ruabbit.mirai

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.util.*
import moe.ruabbit.mirai.config.SettingsConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
object KtorUtils {

    private val okHttpClient = OkHttpClient.Builder().apply {
        retryOnConnectionFailure(true)
        connectTimeout(30000L, TimeUnit.MILLISECONDS)
    }

    // 使用代理的ktor客户端
    @OptIn(KtorExperimentalAPI::class)
    val proxyClient = HttpClient(OkHttp) {
        engine {
            proxy = when (SettingsConfig.proxyConfig) {
                0 -> null
                1 -> ProxyBuilder.http(SettingsConfig.httpProxy.proxy)
                2 -> ProxyBuilder.socks(host = SettingsConfig.socksProxy.host, port = SettingsConfig.socksProxy.port)
                else -> null
            }
            config { okHttpClient }
        }
    }

    // 未使用代理的Ktor客户端
    val normalClient = HttpClient(OkHttp)
    {
        engine { config { okHttpClient } }
    }

    // 安全的关闭客户端, 防止堵塞主线程
    fun closeClient() {
        proxyClient.close()
        normalClient.close()
    }

}
