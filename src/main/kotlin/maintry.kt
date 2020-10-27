package com.blrabbit.mirai.setu

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


fun main() {
    val client = OkHttpClient()
    val request = Request.Builder().get()
        .url("https://i.pixiv.cat/img-original/img/2018/03/26/20/53/37/67928421_p0.png")
        .build()

    var call = client.newCall(request)

    val a: InputStream? = call.execute().body?.byteStream()
    println("Done!")
    val fileName = "67928421_p0.png"
    val testFile = File("." + File.separator + "config" + File.separator + "com.blrabbit.mirai-setu" + File.separator + "Image" + File.separator + fileName)
    val fileParent = testFile.parentFile //返回的是File类型,可以调用exsit()等方法
    if (!fileParent.exists()) {
        fileParent.mkdirs() // 能创建多级目录
    }
    if (!testFile.exists())
        testFile.createNewFile() //有路径才能创建文件
    println("Done!")
    var c = -1
    val buffer = ByteArray(1024 * 1000000000)
    val now = System.currentTimeMillis()
    while ({ if (a != null) {
            c = a.read(buffer)
    };c }() > 0) {
        testFile.appendBytes(buffer.copyOfRange(0, c))
    }
    println("复制完毕，耗时${(System.currentTimeMillis() - now) / 1000}秒")


}


