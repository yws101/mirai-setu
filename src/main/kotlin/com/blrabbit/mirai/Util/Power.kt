package com.blrabbit.mirai.Util

import com.blrabbit.mirai.Util.storge.MySetting
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator

fun checkpower(sender: Member): Boolean {
    if (MySetting.masterid.contains(sender.id))
        return true
    return sender.isOperator()
}
