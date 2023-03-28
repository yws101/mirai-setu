package moe.ruabbit.mirai.setu

import moe.ruabbit.mirai.config.SettingsConfig
import java.util.*

object SetuCoolDownManager {

    /**
     * <群号,上次发送成功时间>
     */
    private val coolDownMap: MutableMap<Long, Date> by lazy {
        mutableMapOf()
    }

    /**
     * 是否冷却完成
     * @return 剩余冷却时间 0代表冷却完成
     */
    fun isCoolDown(groupId: Long): Long {
        val coolDownConfigMs = SettingsConfig.coolDownTime
        if (coolDownConfigMs > 0) {
            coolDownMap[groupId]?.let {
                val now = Date()
                return (coolDownConfigMs - (now.time - it.time)).coerceAtLeast(0) / 1000L
            }
        }
        return 0
    }

    /**
     * 发送成功 进入冷却计算
     */
    fun startCoolDown(groupId: Long) {
        coolDownMap[groupId] = Date()
    }


}