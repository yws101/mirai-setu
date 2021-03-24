package cn.blrabbit.mirai.utils.storge

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Command : ReadOnlyPluginConfig("command") {
    @ValueDescription("修改触发的指令")
    val command_get: MutableList<String> by value(mutableListOf("色图时间", "涩图时间", "涩图来", "色图来"))
    val command_search: MutableList<String> by value(mutableListOf("搜色图", "搜涩图"))
    val command_off: MutableList<String> by value(mutableListOf("关闭插件", "封印"))
    val command_setumode0: MutableList<String> by value(mutableListOf("普通模式", "青少年模式"))
    val command_setumode1: MutableList<String> by value(mutableListOf("R-18模式", "青壮年模式"))
    val command_setumode2: MutableList<String> by value(mutableListOf("混合模式"))

    val command_saucennao: MutableList<String> by value(mutableListOf("以图搜图", "搜图"))
}
