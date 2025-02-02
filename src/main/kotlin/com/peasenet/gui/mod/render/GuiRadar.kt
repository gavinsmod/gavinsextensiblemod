/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
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

import com.peasenet.config.render.RadarConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.mods.render.ModRadar
import com.peasenet.settings.*
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

        val colorTitle = GuiBuilder<Gui>()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.color")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()
        box = GuiBuilder<Gui>()
            .setWidth(width - 161 - paddingX * 2)
            .setHeight(height - (81 - paddingY * 1 + PADDING))
            .setTopLeft(paddingX - PADDING, colorTitle.y - paddingY)
            .setHoverable(false)
            .setTransparency(0.5f)
            .build()
        pos = pos.add(0f, gapY * 4)
        val playerEntityColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.color.player")
            .setColor(config.playerColor)
            .setTopLeft(pos)
            .setCallback { config.playerColor = it.color }
            .buildColorSetting()
        pos = pos.add(playerEntityColor.gui.width + PADDING * 2, 0f)
        val hostileEntityColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.color.hostileMob")
            .setColor(config.hostileMobColor)
            .setTopLeft(pos)
            .setCallback { config.hostileMobColor = it.color }
            .buildColorSetting()
        pos = PointF(paddingX, pos.y + gapY)
        val peacefulEntityColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.color.peacefulMob")
            .setColor(config.peacefulMobColor)
            .setTopLeft(pos)
            .setCallback { config.peacefulMobColor = it.color }
            .buildColorSetting()
        pos = pos.add(peacefulEntityColor.gui.width + PADDING * 2, 0f)

        val entityItemColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.radar.item.color")
            .setColor(config.itemColor)
            .setTopLeft(pos)
            .setCallback { config.itemColor = it.color }
            .buildColorSetting()

        pos = PointF(paddingX, pos.y + gapY)

        val backgroundColor = SettingBuilder<ColorSetting>()
            .setTitle("gavinsmod.settings.background.color")
            .setColor(config.backgroundColor)
            .setTopLeft(pos)
            .setCallback { config.backgroundColor = it.color }
            .buildColorSetting()
        pos = PointF(paddingX, pos.y + gapY)
        val scaleTitle = GuiBuilder<Gui>()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.scale")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()
        pos = pos.add(0f, gapY)
        val scaleSetting = SettingBuilder<ClickSetting>()
            .setTitle("gavinsmod.settings.radar.scale")
            .setTopLeft(pos)
            .setCallback {
                config.scale += 1
                updateScaleText(it, config.scale)
            }
            .buildClickSetting()
        pos = pos.add(scaleSetting.gui.width + PADDING * 2, 0f)

        val pointSizeSetting = SettingBuilder<ClickSetting>()
            .setTitle("gavinsmod.settings.radar.pointsize")
            .setTopLeft(pos)
            .setCallback {
                config.pointSize += 2
                updateScaleText(it, config.pointSize)
            }
            .buildClickSetting()
        pos = PointF(paddingX, pos.y + gapY)
        val toggleTitle = GuiBuilder<Gui>()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.toggle")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)

        val peacefulEntityToggle = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.mob.peaceful")
            .setState(config.isShowPeacefulMob)
            .setTopLeft(pos)
            .setCallback {
                config.isShowPeacefulMob = it.value
            }
            .buildToggleSetting()

        val hostileEntityToggle = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.mob.hostile")
            .setState(config.isShowHostileMob)
            .setTopLeft(pos)
            .setCallback {
                config.isShowHostileMob = it.value
            }
            .buildToggleSetting()

        val itemToggle = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.radar.item")
            .setState(config.isShowItem)
            .setTopLeft(pos)
            .setCallback { config.isShowItem = it.value }
            .buildToggleSetting()
        val playerEntityToggle = SettingBuilder<ToggleSetting>()
            .setTitle("gavinsmod.settings.radar.player")
            .setState(config.isShowPlayer)
            .setTopLeft(pos)
            .setCallback {
                config.isShowPlayer = it.value
            }
            .buildToggleSetting()
        val alphaTitle = GuiBuilder<Gui>()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.alpha")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)


        val backgroundAlpha = SettingBuilder<SlideSetting>()
            .setTitle("gavinsmod.settings.radar.background.alpha")
            .setValue(config.backgroundAlpha)
            .setTopLeft(pos)
            .setCallback { config.backgroundAlpha = it.value }
            .buildSlideSetting()

        pos = pos.add(0f, gapY)

        val pointAlpha = SettingBuilder<SlideSetting>()
            .setTitle("gavinsmod.settings.radar.point.alpha")
            .setValue(config.pointAlpha)
            .setTopLeft(pos)
            .setCallback { config.pointAlpha = it.value }
            .buildSlideSetting()
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

