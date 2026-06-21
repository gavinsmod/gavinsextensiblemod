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
class GuiFreeCam : GuiElement(Component.translatable("gavinsmod.mod.misc.freecam")) {

    private var gui_width = 200
    private val minWidth = 100
    private val m_height = 11 * 5f

    override fun init() {
        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        val screenHeight = Minecraft.getInstance().window.guiScaledHeight

        val offsetX = (screenWidth - gui_width) / 2f
        val offsetY = (screenHeight - m_height) / 2f
        var guiPosition = PointF(offsetX, offsetY)


        addSetting(slideSetting {
            title = "gavinsmod.generic.speed"
            value = getSettings().freeCamSpeed
            callback = { getSettings().freeCamSpeed = it.value }
            topLeft = guiPosition
            width = gui_width.toFloat()
        })

        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.generic.esp"
            state = getSettings().espEnabled
            callback = { getSettings().espEnabled = it.state }
            topLeft = guiPosition
            width = gui_width.toFloat()
        })

        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.generic.tracer"
            state = getSettings().tracerEnabled
            callback = { getSettings().tracerEnabled = it.state }
            topLeft = guiPosition
            width = gui_width.toFloat()
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.generic.color"
            color = getSettings().color
            callback = { getSettings().color = it.color }
            topLeft = guiPosition
            width = gui_width.toFloat()
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(slideSetting {
            title = "gavinsmod.generic.alpha"
            value = 1f
            callback = { getSettings().alpha = it.value }
            topLeft = guiPosition
            width = gui_width.toFloat()
        })
        resizeElements()
        super.init()
    }

    private fun resizeElements() {

        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        var longestWidth = 0f
        for (setting in guis) {
            val width = Minecraft.getInstance().font.width(setting.title)
            if (width > longestWidth) {
                longestWidth = width.toFloat()
            }
        }
        gui_width = (longestWidth + 24f).toInt().coerceAtLeast(minWidth)
        for (setting in guis) {
            setting.width = gui_width.toFloat()
            setting.position = PointF((screenWidth - gui_width) / 2f, setting.position.y)
        }
    }


    private fun getSettings(): FreeCamConfig {
        return Settings.getConfig(ChatCommand.FreeCam)
    }
}