package cn.blrabbit.mirai.Util.storge

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Message : ReadOnlyPluginConfig("Message") {
    @ValueDescription("发送信息的格式，pid(图片pid) p(图片号) uid(作者uid) title(题目) author(作者) originalurl(原始url) r18(是否为r18) width(宽度) height(高度) tags(标签) largeurl(大号缩略图ps.无法保证100%有效)")
    val SetuReply: String by value("pid: %pid%\ntitle: %title%\nauthor: %author%\nurl: %originalurl%\ntags: %tags%")

    @ValueDescription("来自lolicon的报错，code(错误代码)，msg(错误信息)")
    val Lolicode401 by value("lolicon错误代码：%code% 错误信息：%msg%")
    val lolicode404 by value("lolicon错误代码：%code% 错误信息：%msg%")
    val lolicode429 by value("lolicon错误代码：%code% 错误信息：%msg%")
    val lolicodeelse by value("lolicon错误代码：%code%,发生此错误请截取详细信息到github提交issue 错误信息：%msg%")

    @ValueDescription("下载图片出现404自动回复")
    val image404 by value("图片获取失败，可能图片已经被原作者删除")

    @ValueDescription("群友没有权限的时候的自动回复")
    val setunopermission by value("您没有权限操作")

    @ValueDescription("色图插件未启用自动回复")
    val setumodeoff by value("本群尚未开启插件,请联系管理员")

    @ValueDescription("搜色图提醒关键词自动回复")
    val setusearchkey by value("请输入搜索的关键词")

    @ValueDescription("关闭色图插件的自动回复")
    val setumode_1 by value("已经关闭了本群的色图插件")
    val setumode_1to_1 by value("本群尚未启用色图插件,无需再次禁用")

    @ValueDescription("设置普通模式的自动回复")
    val setumode0 by value("切换为普通模式")
    val setumode0to0 by value("本群色图已经为普通模式，无需切换")

    @ValueDescription("设置r-18模式的自动回复")
    val setumode1 by value("切换为R-18模式")
    val setumode1to1 by value("本群色图已经为R-18模式，无需切换")

    @ValueDescription("设置混合模式的自动回复")
    val setumode2 by value("切换为混合模式")
    val setumode2to2 by value("本群色图已经为混合模式，无需切换")
}