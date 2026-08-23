package com.peasenet.gui.mod.render

import com.peasenet.config.render.XrayConfig
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Settings
import com.peasenet.settings.clickSetting
import com.peasenet.settings.slideSetting
import com.peasenet.settings.toggleSetting
import com.peasenet.util.ChatCommand
import net.minecraft.network.chat.Component

/**
 * Settings screen for X-Ray.  Holds all per-mod toggles (culling, liquids),
 * a gamma brightness slider, and a shortcut to the block-list picker.
 *
 * @author GT3CH1
 * @version 08-16-2026
 * @since 08-16-2026
 */
class GuiXraySettings : GuiElement(Component.translatable("gavinsmod.mod.render.xray"), 1) {
    override fun init() {
        addSetting(toggleSetting {
            title = "gavinsmod.settings.xray.culling"
            state = getSettings().blockCulling
            callback = {
                getSettings().blockCulling = it.state
                minecraftClient.reloadRenderer()
            }
        })

        addSetting(toggleSetting {
            title = "gavinsmod.settings.xray.liquids"
            state = getSettings().showLiquids
            callback = {
                getSettings().showLiquids = it.state
                minecraftClient.reloadRenderer()
            }
        })

        addSetting(slideSetting {
            title = "gavinsmod.settings.xray.gamma"
            value = getSettings().gamma
            callback = {
                getSettings().gamma = it.value
            }
        })

        addSetting(clickSetting {
            title = "gavinsmod.settings.xray.blocks"
            callback = {
                minecraftClient.setScreen(GuiXray())
            }
        })

        super.init()
    }

    private fun getSettings(): XrayConfig = Settings.getConfig(ChatCommand.Xray)
}




