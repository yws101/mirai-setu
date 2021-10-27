package moe.ruabbit.mirai.setu

import io.ktor.util.*
import moe.ruabbit.mirai.PluginMain.checkPermission
import moe.ruabbit.mirai.config.CommandConfig
import moe.ruabbit.mirai.config.MessageConfig
import moe.ruabbit.mirai.config.SettingsConfig
import moe.ruabbit.mirai.data.SetuData
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.nextMessage

@KtorExperimentalAPI
fun setuListenerRegister() {
    GlobalEventChannel.subscribeGroupMessages {

        //色图时间
        always {
            if (CommandConfig.get.contains(message.contentToString())) {
                if (SetuData.groupPolicy[group.id] != null) {
                    val coolDown = SetuCoolDownManager.isCoolDown(group.id)
                    if (coolDown > 0) {
                        group.sendMessage(message.quote() + String.format(MessageConfig.setuCoolDownNotReady, coolDown))
                        return@always
                    }

                    when (SettingsConfig.requestApi) {
                        FantasyZoneRequester.TAG -> {
                            val setu = FantasyZoneRequester(group, source)
                            if (SettingsConfig.enableFetchingMsg) {
                                group.sendMessage(message.quote() + "Fetching setu...")
                            }
                            if (setu.requestSetu()){
                                setu.sendSetu()
                                SetuCoolDownManager.startCoolDown(group.id)
                            }
                        }
                        LoliconRequester.TAG -> {
                            val setu = LoliconRequester(group, source)
                            if (SettingsConfig.enableFetchingMsg) {
                                group.sendMessage(message.quote() + "Fetching setu...")
                            }
                            if (setu.requestSetu()){
                                setu.sendSetu()
                                SetuCoolDownManager.startCoolDown(group.id)
                            }
                        }
                        else -> {
                            group.sendMessage(message.quote() + "requestApi 配置错误")
                        }
                    }
                } else {
                    group.sendMessage(message.quote() + MessageConfig.setuModeOff)
                }
            }
        }

        //搜色图
        always {
            CommandConfig.search.startWith(message.contentToString()).let {
                if (it != null) {
                    if (SetuData.groupPolicy[group.id] != null) {
                        val coolDown = SetuCoolDownManager.isCoolDown(group.id)
                        if (coolDown > 0) {
                            group.sendMessage(message.quote() + String.format(MessageConfig.setuCoolDownNotReady, coolDown))
                            return@always
                        }
                        val msg = it.ifEmpty {
                            group.sendMessage(message.quote() + MessageConfig.setuSearchKeyNotSet)
                            nextMessage().contentToString()
                        }
                        when (SettingsConfig.searchApi) {
                            FantasyZoneRequester.TAG -> {
                                val setu = FantasyZoneRequester(group, source)
                                if (SettingsConfig.enableFetchingMsg) {
                                    group.sendMessage(message.quote() + "Fetching setu...")
                                }
                                if (setu.requestSetu(msg)){
                                    setu.sendSetu()
                                    SetuCoolDownManager.startCoolDown(group.id)
                                }

                            }
                            LoliconRequester.TAG -> {
                                val setu = LoliconRequester(group, source)
                                if (SettingsConfig.enableFetchingMsg) {
                                    group.sendMessage(message.quote() + "Fetching setu...")
                                }
                                if (setu.requestSetu(msg)){
                                    setu.sendSetu()
                                    SetuCoolDownManager.startCoolDown(group.id)
                                }

                            }
                            else -> {
                                group.sendMessage(message.quote() + "requestApi 配置错误")
                            }
                        }
                    } else {
                        group.sendMessage(message.quote() + MessageConfig.setuModeOff)
                    }
                }
            }
        }
    }
}

// 判断词组开头是否包含，不包含返回null
private fun MutableList<String>.startWith(contentToString: String): String? {
    this.forEach {
        if (contentToString.startsWith(it)) {
            return contentToString.replace(it, "").replace(" ", "")
        }
    }
    return null
}
