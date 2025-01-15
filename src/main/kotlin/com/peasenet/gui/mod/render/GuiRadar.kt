/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.gui.mod.render

import com.peasenet.config.RadarConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.mods.render.ModRadar
import com.peasenet.settings.ClickSetting
import com.peasenet.settings.Setting
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.ChatCommand
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * A GUI that allows the user to modify settings for the radar mod. This interface will show a preview of what the
 * radar will look like when the user activates the mod.
 *
 * @see ModRadar
 * @see GuiElement
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 06/18/2023
 */
class GuiRadar : GuiElement(Text.translatable("gavinsmod.mod.render.radar")) {

    private lateinit var box: Gui

    private var offsetX: Int = 0
    private var offsetY: Int = 0
    private var paddingX: Float = 0.0f
    private var paddingY: Float = 0.0f

    private companion object {
        const val PADDING: Float = 5.0f
        var settings: ArrayList<Setting> = ArrayList()
        var visible: Boolean = false
        val config = Settings.getConfig<RadarConfig>(ChatCommand.Radar.chatCommand)
    }

    override fun close() {
        super.close()
        visible = false
    }

    @Suppress("DuplicatedCode")
    override fun init() {
        visible = true
        this.parent = GavinsMod.guiSettings
        settings.clear()
        guis.clear()
        offsetX = client!!.window.scaledWidth / 2 - (this.width / 2)
        offsetY = client!!.window.scaledHeight / 2 - (this.height / 2)
        paddingX = PADDING + PADDING
        paddingY = offsetY + PADDING

        val gapY = 12f
        var pos = PointF(paddingX, paddingY + gapY)

        val colorTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.color")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()
        box = GuiBuilder()
            .setWidth(width - 161 - paddingX * 2)
            .setHeight(height - (81 - paddingY * 1 + PADDING))
            .setTopLeft(paddingX - PADDING, colorTitle.y - paddingY)
            .setHoverable(false)
            .setTransparency(0.5f)
            .build()
        pos = pos.add(0f, gapY * 4)
        val playerEntityColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.player.color")
            .setColor(config.playerColor)
            .setTopLeft(pos)
            .buildColorSetting()
        playerEntityColor.setCallback { config.playerColor = playerEntityColor.color }
        pos = pos.add(playerEntityColor.gui.width + PADDING * 2, 0f)
        val hostileEntityColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.hostile.color")
            .setColor(config.hostileMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        hostileEntityColor.setCallback {
            config.hostileMobColor = hostileEntityColor.color
        }

        pos = PointF(paddingX, pos.y + gapY)

        val peacefulEntityColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.peaceful.color")
            .setColor(config.peacefulMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        peacefulEntityColor.setCallback {
            config.peacefulMobColor = peacefulEntityColor.color
        }

        pos = pos.add(peacefulEntityColor.gui.width + PADDING * 2, 0f)

        val entityItemColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.item.color")
            .setColor(config.itemColor)
            .setTopLeft(pos)
            .buildColorSetting()
        entityItemColor.setCallback { config.itemColor = entityItemColor.color }

        pos = PointF(paddingX, pos.y + gapY)

        val backgroundColor = SettingBuilder()
            .setTitle("gavinsmod.settings.background.color")
            .setColor(config.backgroundColor)
            .setTopLeft(pos)
            .buildColorSetting()
        backgroundColor.setCallback { config.backgroundColor = backgroundColor.color }

        pos = PointF(paddingX, pos.y + gapY)

        val scaleTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.scale")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)


        val scaleSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.scale")
            .setTopLeft(pos)
            .buildClickSetting()
        scaleSetting.setCallback {
            config.scale += 1
            updateScaleText(scaleSetting, config.scale)
        }

        pos = pos.add(scaleSetting.gui.width + PADDING * 2, 0f)

        val pointSizeSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.pointsize")
//            .setCallback(this::togglePointSize)
            .setTopLeft(pos)
            .buildClickSetting()
        pointSizeSetting.setCallback {
            config.updatePointSizeCallback()
            updateScaleText(pointSizeSetting, config.pointSize)
        }
        pos = PointF(paddingX, pos.y + gapY)

        val toggleTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.toggle")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)

        val peacefulEntityToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.peaceful")
            .setState(config.isShowPeacefulMob)
            .setTopLeft(pos)
            .buildToggleSetting()
        peacefulEntityToggle.setCallback {
            config.isShowPeacefulMob = peacefulEntityToggle.value
        }


        val hostileEntityToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.hostile")
            .setState(config.isShowHostileMob)
            .setTopLeft(pos)
            .buildToggleSetting()
        hostileEntityToggle.setCallback {
            config.isShowHostileMob = hostileEntityToggle.value
        }

        val itemToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.item")
            .setState(config.isShowItem)
            .setTopLeft(pos)
            .buildToggleSetting()
        itemToggle.setCallback { config.isShowItem = itemToggle.value }

        val playerEntityToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.player")
            .setState(config.isShowPlayer)
            .setTopLeft(pos)
            .buildToggleSetting()
        playerEntityToggle.setCallback {
            config.isShowPlayer = playerEntityToggle.value
        }

        val alphaTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.alpha")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)


        val backgroundAlpha = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.background.alpha")
            .setValue(config.backgroundAlpha)
            .setTopLeft(pos)
            .buildSlider()
        backgroundAlpha.setCallback { config.backgroundAlpha = backgroundAlpha.value }

        pos = pos.add(0f, gapY)

        val pointAlpha = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.point.alpha")
            .setValue(config.pointAlpha)
            .setTopLeft(pos)
            .buildSlider()
        pointAlpha.setCallback { config.pointAlpha = pointAlpha.value }
        // add all settings to the list
        guis.add(playerEntityColor.gui)
        guis.add(hostileEntityColor.gui)
        guis.add(peacefulEntityColor.gui)
        guis.add(entityItemColor.gui)
        guis.add(backgroundColor.gui)
        guis.add(scaleSetting.gui)
        guis.add(pointSizeSetting.gui)
        guis.add(peacefulEntityToggle.gui)
        guis.add(hostileEntityToggle.gui)
        guis.add(itemToggle.gui)
        guis.add(playerEntityToggle.gui)
        guis.add(backgroundAlpha.gui)
        guis.add(pointAlpha.gui)

        var maxWidth = 0
        for (gui in guis) {
            if (gui.title == null)
                continue
            if (client!!.textRenderer.getWidth(gui.title) > maxWidth) {
                maxWidth = client!!.textRenderer.getWidth(gui.title)
            }
        }

        guis.forEach {
            it.width = (maxWidth + 10).toFloat()
        }
        pos = PointF(paddingX, paddingY + gapY * 2)
        playerEntityColor.gui.position = pos
        pos = pos.add(playerEntityColor.gui.width + PADDING, 0f)
        hostileEntityColor.gui.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        peacefulEntityColor.gui.position = pos
        pos = pos.add(peacefulEntityColor.gui.width + PADDING, 0f)
        entityItemColor.gui.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        backgroundColor.gui.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        scaleTitle.position = (pos)
        pos = PointF(paddingX, pos.y + gapY)
        pointSizeSetting.gui.position = pos
        pos = pos.add(pointSizeSetting.gui.width + PADDING, 0f)
        scaleSetting.gui.position = pos
        pos = PointF(paddingX, pos.y + gapY)

        toggleTitle.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        playerEntityToggle.gui.position = pos
        pos = pos.add(playerEntityToggle.gui.width + PADDING, 0f)
        hostileEntityToggle.gui.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        peacefulEntityToggle.gui.position = pos
        pos = pos.add(peacefulEntityToggle.gui.width + PADDING, 0f)
        itemToggle.gui.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        alphaTitle.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        backgroundAlpha.gui.position = pos
        pos = pos.add(backgroundAlpha.gui.width + PADDING, 0f)
        pointAlpha.gui.position = pos

        guis.add(0, box)
        guis.add(colorTitle)
        guis.add(alphaTitle)
        guis.add(toggleTitle)
        guis.add(scaleTitle)
        guis.add(alphaTitle)
        updateScaleText(scaleSetting, config.scale)
        updateScaleText(pointSizeSetting, config.pointSize)
        super.init()
    }

    /**
     * Callback method for the scale setting.
     */
//    private fun increaseScale() {
//        Settings.getConfig<RadarConfig>("radar").scale += 1
//        updateScaleText(scaleSetting, Settings.getConfig<RadarConfig>("radar").scale)
//    }

    /**
     * Callback for the point size setting.
     */
//    private fun togglePointSize() {
//        Settings.getConfig<RadarConfig>("radar").updatePointSizeCallback()
//        updateScaleText(pointSizeSetting, Settings.getConfig<RadarConfig>("radar").pointSize)
//    }

    /**
     * Callback for update the scale text.
     */
    private fun updateScaleText(setting: ClickSetting, value: Int) {
        setting.gui.title =
            (Text.translatable(setting.gui.translationKey).append(Text.literal(" (%s)".format(value))))
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(drawContext, mouseX, mouseY, delta)
        val mod = Mods.getMod<ModRadar>(ChatCommand.Radar)
        mod.onRenderInGameHud(drawContext, delta, true)
    }
}

