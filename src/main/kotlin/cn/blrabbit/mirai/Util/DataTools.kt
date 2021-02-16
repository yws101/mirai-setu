package cn.blrabbit.mirai.Util

//数组判断
fun String.startsWith(list: MutableList<String>): Boolean {
    list.forEach {
        if(this.startsWith(it))
            return true
    }
    return false
}

