package com.peasenet.gui.mod.misc

import com.peasenet.config.misc.FreeCamConfig
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.Settings
import com.peasenet.settings.colorSetting
import com.peasenet.settings.slideSetting
import com.peasenet.settings.toggleSetting
import com.peasenet.util.ChatCommand
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

/**
 *
 * @author GT3CH1
 * @version 06-21-2026
 * @since 06-21-2026 
 */
class GuiFreeCam : GuiElement(Component.translatable("gavinsmod.mod.misc.freecam"), 1) {
    override fun init() {
        addSetting(slideSetting {
            title = "gavinsmod.generic.speed"
            value = getSettings().freeCamSpeed
            callback = { getSettings().freeCamSpeed = it.value }
        })

        addSetting(slideSetting {
            title = "gavinsmod.generic.alpha"
            value = 1f
            callback = { getSettings().alpha = it.value }
        })

        addSetting(toggleSetting {
            title = "gavinsmod.generic.esp"
            state = getSettings().espEnabled
            callback = { getSettings().espEnabled = it.state }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.generic.tracer"
            state = getSettings().tracerEnabled
            callback = { getSettings().tracerEnabled = it.state }
        })
        addSetting(colorSetting {
            title = "gavinsmod.generic.color"
            color = getSettings().color
            callback = { getSettings().color = it.color }
        })
        super.init()
    }
    private fun getSettings(): FreeCamConfig {
        return Settings.getConfig(ChatCommand.FreeCam)
    }
}