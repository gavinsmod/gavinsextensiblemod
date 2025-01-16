package com.peasenet.mods.esp

import com.peasenet.gavui.color.Color
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.ChatCommand
import net.minecraft.block.entity.BeehiveBlockEntity
import net.minecraft.block.entity.SignBlockEntity

/**
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 01-14-2025
 */
class ModSignEsp : BlockEntityEsp<SignBlockEntity>(
    "Sign ESP",
    "gavinsmod.mod.esp.sign",
    ChatCommand.SignEsp.chatCommand,
    { it is SignBlockEntity }) {
    init {
        val colorSetting =
            SettingBuilder().setTitle("gavinsmod.settings.color.sign").setColor(config.signColor)
                .buildColorSetting()
        colorSetting.setCallback { config.signColor = colorSetting.color }
        addSetting(colorSetting)
    }

    override fun getColor(): Color = config.signColor
}