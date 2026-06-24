package com.peasenet.gui.mod.render

import com.peasenet.config.misc.FpsColorConfig
import com.peasenet.gui.GuiElement
import com.peasenet.main.Settings
import com.peasenet.settings.colorSetting
import com.peasenet.settings.toggleSetting
import com.peasenet.util.ChatCommand
import net.minecraft.network.chat.Component

/**
 *
 * @author GT3CH1
 * @version 06-21-2026
 * @since 06-21-2026 
 */
class GuiFpsCounter : GuiElement(Component.translatable("gavinsmod.mod.misc.fpscounter"), 1) {
    override fun init() {

        addSetting(toggleSetting {
            title = "gavinsmod.settings.misc.fpscolors.enabled"
            state = getSettings().isColorsEnabled
            callback = { getSettings().isColorsEnabled = it.state }
        })
        addSetting(colorSetting {
            title = "gavinsmod.settings.misc.fps.color.slow"
            color = getSettings().slowFps
            callback = { getSettings().slowFps = it.color }
        })
        addSetting(colorSetting {
            title = "gavinsmod.settings.misc.fps.color.ok"
            color = getSettings().okFps
            callback = { getSettings().okFps = it.color }
        })
        addSetting(colorSetting {
            title = "gavinsmod.settings.misc.fps.color.fast"
            color = getSettings().fastFps
            callback = { getSettings().fastFps = it.color }
        })
        super.init()
    }

    private fun getSettings(): FpsColorConfig {
        return Settings.getConfig(ChatCommand.FpsCounter)
    }
}