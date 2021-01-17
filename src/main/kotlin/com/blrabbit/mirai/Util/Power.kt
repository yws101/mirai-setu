package com.blrabbit.mirai.Util

import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator

fun checkpower(sender:Member): Boolean {
    if (MySetting.masterid.contains(sender.id))
        return true
    return sender.isOperator()
}