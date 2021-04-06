package cn.blrabbit.mirai.utils

import cn.blrabbit.mirai.MiraiSetuMain
import cn.blrabbit.mirai.utils.storge.MySetting
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator

// 权限判断（获取以后会搞上更好的方法？）
fun checkpower(sender: Member): Boolean {
    when (MySetting.mastermode) {
        1 -> {
            if (MySetting.masterid.contains(sender.id))
                return true
        }
        2 -> {
            if (MySetting.masterid.contains(sender.id))
                return true
            return sender.isOperator()
        }
        else -> {
            MiraiSetuMain.logger.warning("权限设置信息错误，请检查权限模式配置")
            return false
        }
    }
    return false
}
