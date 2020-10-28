package com.blrabbit.mirai.setu

import com.beust.klaxon.Klaxon
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.sendImage
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.blrabbit.mirai-setu",
        version = "0.1.0"
    )
) {
    override fun onEnable() {
        MySetting.reload()//初始化配置数据
        Mydata.reload()//初始化插件数据
        val R18g = R18Group()
        logger.info { "提示：${MySetting.name}加载完成" }
        if(MySetting.APIKEY.isEmpty()){
            logger.warning{ "未设置lolicon的APIKEY，可能会遇到调用上限的问题" }
            logger.warning{ "申请地址: https://api.lolicon.app/#/setu" }
            logger.warning{ "请到 config/com.blrabbit.mirai-setu/setu-config.yml 添加APIKEY" }
        }

        subscribeGroupMessages{

            /*case("测试发图"){
                val client = OkHttpClient()
                val request = Request.Builder().get()
                    .url("https://i.pixiv.cat/img-original/img/2018/03/26/20/53/37/67928421_p0.png")
                    .build()

                var call = client.newCall(request)

                val a: InputStream? = call.execute().body?.byteStream()
                if (a != null) {
                    sendImage(a)
                }
            }*/
            //获取色图的触发词
            case(MySetting.command_get){
                sender.group.sendMessage("正在获取图片，请稍后")
                //json解析到result
                val result = Klaxon().parse<Image>(Getsetu(R18g.check(group.id)))
                if (result != null) {
                    if (result.code == 0) {
                        Mydata.quota = result.quota
                        //向群内发送信息
                        sender.group.sendMessage("pid：${result.data[0].pid}\n" +
                            "title: ${result.data[0].title}\n" +
                            "author: ${result.data[0].author}\n" +
                            "url: ${result.data[0].url}\n" +
                            "tags: ${result.data[0].tags}")
                        logger.info("剩余调用次数 ${result.quota}")

                        val a: InputStream? = Downsetu(result.data[0].url)
                        //发送图片，可能会被腾讯吞掉
                        if (a != null) {
                            group.sendImage(a)
                        }
                    }else{
                        reply("超出调用次数")
                    }
                }
            }
            //启用R18模式
            case(MySetting.command_R18on){
                reply("警告，R18限制已解除")
                R18g.add(group.id)
            }
            //关闭R18模式
            case(MySetting.command_R18off)    {
                reply("R18已关闭")
                R18g.del(group.id)
            }

            startsWith(MySetting.command_search){
                logger.warning("输出消息${message.contentToString().removePrefix(MySetting.command_search).removePrefix(" ")}")
                sender.group.sendMessage("正在获取图片，请稍后")
                //json解析到result
                val result = Klaxon()
                    .parse<Image>(Getsetu(R18g.check(group.id), message.contentToString().removePrefix(MySetting.command_search).removePrefix(" ")))
                if (result != null) {
                    if (result.code == 0) {
                        Mydata.quota = result.quota
                        //向群内发送信息
                        sender.group.sendMessage("pid：${result.data[0].pid}\n" +
                            "title: ${result.data[0].title}\n" +
                            "author: ${result.data[0].author}\n" +
                            "url: ${result.data[0].url}\n" +
                            "tags: ${result.data[0].tags}")
                        logger.info("剩余调用次数 ${result.quota}")

                        val a: InputStream? = Downsetu(result.data[0].url)
                        if (a != null) {
                            group.sendImage(a)
                        }
                    }else{
                        reply("超出调用次数")
                    }
                }
            }

            case("setu状态检查"){
                sender.group.sendMessage("剩余调用次数：${Mydata.quota}\n" +
                    "当前R18模式 ${R18g.check(group.id)}")
            }


        }
    }
}

class R18Group{
    var people = ArrayList<Long>()

    fun add(qq: Long){
        people.add(qq)
    }

    fun del(qq: Long){
        people.remove(qq)
    }

    fun check(qq:Long): Int {
        for (i in 0 until people.size)
            if (people[i] == qq)
                return 1
        return 0
    }


}

//配置文件存储
object MySetting : AutoSavePluginConfig(){
    val name by value("setu")
    val APIKEY by value("")
    val command_get by value("色图时间")
    val command_R18off by value("青少年模式")
    val command_R18on by value("青壮年模式")
    val command_search by value("搜色图")
}
//配置数据存储
object Mydata : AutoSavePluginData(){
    var quota by value(-1)
}
