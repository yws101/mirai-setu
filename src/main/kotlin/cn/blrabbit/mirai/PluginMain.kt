package cn.blrabbit.mirai

import cn.blrabbit.mirai.config.CommandConfig
import cn.blrabbit.mirai.config.MessageConfig
import cn.blrabbit.mirai.config.SettingsConfig
import cn.blrabbit.mirai.data.SetuData
import cn.blrabbit.mirai.manga.fantasyZoneRegister
import cn.blrabbit.mirai.search.sauceNaoRegister
import cn.blrabbit.mirai.setu.setuListenerRegister
import io.ktor.util.*
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.blrabbit.mirai",
        name = "mirai-setu",
        version = "2.0-SNAPSHOT"
    )
) {

    private lateinit var adminPermission: Permission

    @KtorExperimentalAPI
    override fun onEnable() {
        SettingsConfig.reload() //初始化设置数据
        SetuData.reload()    //初始化配置数据
        CommandConfig.reload()   //初始化插件指令
        MessageConfig.reload()   //初始化自定义回复
        adminPermission = PermissionService.INSTANCE.register(
            PermissionId("setu", "admin"),
            "Admin Permission"
        )
        setuListenerRegister()
        sauceNaoRegister()
        fantasyZoneRegister()
    }

    @KtorExperimentalAPI
    override fun onDisable() {
        // 关闭ktor客户端，防止堵塞线程无法关闭
        KtorUtils.closeClient()
    }

    // 权限判断（获取以后会搞上更好的方法？）
    fun checkPermission(sender: Member): Boolean {
        when (SettingsConfig.permitMode) {
            0 -> {
                return true
            }
            1 -> {
                if (SettingsConfig.masterId.contains(sender.id))
                    return true
            }
            2 -> {
                if (SettingsConfig.masterId.contains(sender.id))
                    return true
                return sender.isOperator()
            }
            3 -> {
                return sender.permitteeId.hasPermission(adminPermission)
            }
            else -> {
                PluginMain.logger.warning("权限设置信息错误，请检查权限模式配置")
                return false
            }
        }
        return false
    }
}
