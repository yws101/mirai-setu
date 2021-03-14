package cn.blrabbit.mirai.Util

import cn.blrabbit.mirai.Util.storge.MySetting
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator

// 权限判断（获取以后会搞上更好的方法？）
fun checkpower(sender: Member): Boolean {
    if (MySetting.masterid.contains(sender.id))
        return true
    return sender.isOperator()
}
