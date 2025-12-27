package com.peasenet.gui.mod.render

import com.peasenet.config.esp.OreEspConfig
import com.peasenet.config.render.HealthTagConfig
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsModClient
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
 * @version 12-24-2025
 * @since 12-24-2025
 */
class GuiHealthTag : GuiElement(Component.translatable("gavinsmod.mod.render.hptags")) {

    private var m_width = 150
    private val m_height = 13 * 8f
    override fun init() {
        this.parent = GavinsModClient.guiSettings
        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        val screenHeight = Minecraft.getInstance().window.guiScaledHeight

        val offsetX = (screenWidth - m_width) / 2f
        val offsetY = (screenHeight - m_height) / 2f
        var guiPosition = PointF(offsetX, offsetY)
        addSetting(colorSetting {
            title = "gavinsmod.generic.background"
            color = getSettings().backgroundColor
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().backgroundColor = it.color
            }
        })
        guiPosition = guiPosition.add(0f, 13f);
        addSetting(toggleSetting {
            title = "gavinsmod.settings.mob.peaceful"
            state = getSettings().friendlyMobs
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().friendlyMobs = it.state
            }
        })
        guiPosition = guiPosition.add(0f, 13f);
        addSetting(toggleSetting {
            title = "gavinsmod.settings.mob.hostile"
            state = getSettings().hostileMobs
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().hostileMobs = it.state
            }
        })

        guiPosition = guiPosition.add(0f, 13f);
        addSetting(slideSetting {
            title = "gavinsmod.generic.alpha"
            value = getSettings().alpha
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().alpha = it.value
            }
        })
        // Health Colors
        guiPosition = guiPosition.add(0f, 13f);
        addSetting(colorSetting {
            title = "gavinsmod.settings.hptags.color.high"
            color = getSettings().highHealthColor
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().highHealthColor = it.color
            }
        })
        guiPosition = guiPosition.add(0f, 13f);
        addSetting(colorSetting {
            title = "gavinsmod.settings.hptags.color.mid"
            color = getSettings().midHealthColor
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().midHealthColor = it.color
            }
        })
        guiPosition = guiPosition.add(0f, 13f);
        addSetting(colorSetting {
            title = "gavinsmod.settings.hptags.color.midlow"
            color = getSettings().midLowHealthColor
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().midLowHealthColor = it.color
            }
        })
        guiPosition = guiPosition.add(0f, 13f);
        addSetting(colorSetting {
            title = "gavinsmod.settings.hptags.color.low"
            color = getSettings().lowHealthColor
            topLeft = guiPosition
            width = m_width.toFloat()
            callback = {
                getSettings().lowHealthColor = it.color
            }
        })

        super.init()
    }

    private fun getSettings(): HealthTagConfig {
        return Settings.getConfig(ChatCommand.HealthTag)
    }
}