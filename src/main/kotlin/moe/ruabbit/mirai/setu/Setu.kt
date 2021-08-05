package moe.ruabbit.mirai.setu

import net.mamoe.mirai.contact.Contact

/**
 * 色图的抽象类
 * @author bloodt-rabbit
 * @version 1.3 2021/8/5
 * @param subject 传入的消息来自对象，供发送和上传图片使用
 * @see Loliconv2Requester
 */
abstract class Setu(subject: Contact) {
    /**
     * 请求获取随机色图
     * @param num 请求的色图数量
     */
    abstract suspend fun requestSetu(num: Int)

    /**
     * 请求获取包含tag的色图
     * @param tags 获取的关键词
     * @param num 请求的色图数量
     */
    abstract suspend fun requestSetu(tags: List<String>, num: Int)

    /**
     * 发送图片以及对应的介绍信息
     */
    abstract suspend fun sendmessage()

}