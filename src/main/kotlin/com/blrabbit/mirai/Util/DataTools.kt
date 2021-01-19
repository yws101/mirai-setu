package com.blrabbit.mirai.Util

//数组判断
fun String.startsWith(list: MutableList<String>): Boolean {
    list.forEach {
        if(this.startsWith(it))
            return true
    }
    return false
}

//布尔型转short星自定义
fun Boolean.toShort(): Short {
    return if (this) 1 else 0
}
