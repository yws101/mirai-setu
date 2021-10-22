package moe.ruabbit.mirai.config

import moe.ruabbit.mirai.PluginMain
import moe.ruabbit.mirai.data.SetuData
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages


fun registerConfigListener() {

    GlobalEventChannel.subscribeGroupMessages {

        // 重载配置
        "mirai-setu reload"{
            PluginMain.reloadConfig()
            group.sendMessage(MessageConfig.setuConfigReloadComplete)
        }

        // 涩图插件开关控制
        always {
            // 关闭涩图插件
            if (CommandConfig.off.contains(message.contentToString())) {
                if (PluginMain.checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == null) {
                        group.sendMessage(MessageConfig.setuOffAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuOff)
                        SetuData.groupPolicy.remove(group.id)
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            // 设置为普通模式
            if (CommandConfig.setSafeMode.contains(message.contentToString())) {
                if (PluginMain.checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 0) {
                        group.sendMessage(MessageConfig.setuSafeAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuSafe)
                        SetuData.groupPolicy[group.id] = 0
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            // 设置为R-19模式
            if (CommandConfig.setNsfwMode.contains(message.contentToString())) {
                if (PluginMain.checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 1) {
                        group.sendMessage(MessageConfig.setuNsfwAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuNsfw)
                        SetuData.groupPolicy[group.id] = 1
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            // 设置为混合模式
            if (CommandConfig.setBothMode.contains(message.contentToString())) {
                if (PluginMain.checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 2) {
                        group.sendMessage(MessageConfig.setuBothAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuBoth)
                        SetuData.groupPolicy[group.id] = 2
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
        }
    }

}