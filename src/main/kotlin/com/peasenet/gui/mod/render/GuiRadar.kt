/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.peasenet.gui.mod.render

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.settings.*
import net.minecraft.text.Text

/**
 *
 * @author gt3ch1
 * @version 06/18/2023
 */
class GuiRadar() : GuiElement(Text.translatable("gavinsmod.mod.render.radar")) {

    private lateinit var playerEntityColor: ColorSetting
    private lateinit var hostileEntityColor: ColorSetting
    private lateinit var peacefulEntityColor: ColorSetting
    private lateinit var entityItemColor: ColorSetting
    private lateinit var waypointColor: ColorSetting
    private lateinit var backgroundColor: ColorSetting
    private lateinit var scaleSetting: ClickSetting
    private lateinit var pointSizeSetting: ClickSetting
    private lateinit var peacefulEntityToggle: ToggleSetting
    private lateinit var hostileEntityToggle: ToggleSetting
    private lateinit var itemToggle: ToggleSetting
    private lateinit var playerEntityToggle: ToggleSetting
    private lateinit var waypointToggle: ToggleSetting
    private lateinit var waypointColors: ToggleSetting
    private lateinit var backgroundAlpha: SlideSetting
    private lateinit var pointAlpha: SlideSetting

    private lateinit var box: Gui

    private lateinit var colorTitle: Gui
    private lateinit var scaleTitle: Gui
    private lateinit var toggleTitle: Gui
    private lateinit var alphaTitle: Gui

    private var offsetX: Int = 0
    private var offsetY: Int = 0
    private var paddingX: Float = 0.0f
    private var paddingY: Float = 0.0f
    private val width = 125 * 2
    private val height = 14 * 13


    companion object {
        const val padding: Float = 5.0f
        var settings: ArrayList<Setting> = ArrayList()
        var visible: Boolean = false
    }

    override fun close() {
        super.close()
        visible = false
    }

    override fun init() {
        visible = true
        this.parent = GavinsMod.guiSettings
        settings.clear()
        guis.clear()
        offsetX = client!!.window.scaledWidth / 2 - (this.width / 2)
        offsetY = client!!.window.scaledHeight / 2 - (this.height / 2)
        paddingX = padding + padding
        paddingY = offsetY + padding
        box = GuiBuilder()
            .setWidth(width)
            .setHeight(height)
            .setTopLeft(padding, offsetY.toFloat())
            .setHoverable(false)
            .setTransparency(0.9f)
            .build()
        val gapY = 12f
        val gapX = 12f
        var pos = PointF(paddingX, paddingY)

        colorTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.color")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)

        playerEntityColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.player.color")
            .setColor(GavinsMod.radarConfig.playerColor)
            .setTopLeft(pos)
            .buildColorSetting()
        playerEntityColor.setCallback { GavinsMod.radarConfig.playerColor = playerEntityColor.color }

        pos = pos.add(playerEntityColor.gui.width + padding * 2, 0f)

        hostileEntityColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.hostile.color")
            .setColor(GavinsMod.radarConfig.hostileMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        hostileEntityColor.setCallback { GavinsMod.radarConfig.hostileMobColor = hostileEntityColor.color }

        pos = PointF(paddingX, pos.y + gapY)

        peacefulEntityColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.peaceful.color")
            .setColor(GavinsMod.radarConfig.peacefulMobColor)
            .setTopLeft(pos)
            .buildColorSetting()
        peacefulEntityColor.setCallback { GavinsMod.radarConfig.peacefulMobColor = peacefulEntityColor.color }

        pos = pos.add(peacefulEntityColor.gui.width + padding * 2, 0f)

        entityItemColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.item.color")
            .setColor(GavinsMod.radarConfig.itemColor)
            .setTopLeft(pos)
            .buildColorSetting()
        entityItemColor.setCallback { GavinsMod.radarConfig.itemColor = entityItemColor.color }

        pos = PointF(paddingX, pos.y + gapY)

        waypointColor = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.waypoint.color")
            .setColor(GavinsMod.radarConfig.waypointColor)
            .setTopLeft(pos)
            .buildColorSetting()
        waypointColor.setCallback { GavinsMod.radarConfig.waypointColor = waypointColor.color }

        backgroundColor = SettingBuilder()
            .setTitle("gavinsmod.settings.background.color")
            .setColor(GavinsMod.radarConfig.backgroundColor)
            .setTopLeft(pos)
            .buildColorSetting()
        backgroundColor.setCallback { GavinsMod.radarConfig.backgroundColor = backgroundColor.color }

        pos = PointF(paddingX, pos.y + gapY)

        scaleTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.scale")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)


        scaleSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.scale")
            .setCallback(this::increaseScale)
            .setTopLeft(pos)
            .buildClickSetting()

        pos = pos.add(scaleSetting.gui.width + padding * 2, 0f)

        pointSizeSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.pointsize")
            .setCallback(this::togglePointSize)
            .setTopLeft(pos)
            .buildClickSetting()

        pos = PointF(paddingX, pos.y + gapY)

        toggleTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.toggle")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)

        peacefulEntityToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.peaceful")
            .setState(GavinsMod.radarConfig.isShowPeacefulMob)
            .setTopLeft(pos)
            .buildToggleSetting()
        peacefulEntityToggle.setCallback { GavinsMod.radarConfig.isShowPeacefulMob = peacefulEntityToggle.value }


        hostileEntityToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.mob.hostile")
            .setState(GavinsMod.radarConfig.isShowHostileMob)
            .setTopLeft(pos)
            .buildToggleSetting()
        hostileEntityToggle.setCallback { GavinsMod.radarConfig.isShowHostileMob = hostileEntityToggle.value }

        itemToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.item")
            .setState(GavinsMod.radarConfig.isShowItem)
            .setTopLeft(pos)
            .buildToggleSetting()
        itemToggle.setCallback { GavinsMod.radarConfig.isShowItem = itemToggle.value }

        waypointToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.waypoints")
            .setState(GavinsMod.radarConfig.isShowWaypoint)
            .setTopLeft(pos)
            .buildToggleSetting()
        waypointToggle.setCallback { GavinsMod.radarConfig.isShowWaypoint = waypointToggle.value }

        playerEntityToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.player")
            .setState(GavinsMod.radarConfig.isShowPlayer)
            .setTopLeft(pos)
            .buildToggleSetting()
        playerEntityToggle.setCallback { GavinsMod.radarConfig.isShowPlayer = playerEntityToggle.value }

        waypointColors = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.waypoint.usecolor")
            .setState(GavinsMod.radarConfig.isUseWaypointColor)
            .setTopLeft(pos)
            .buildToggleSetting()
        waypointColors.setCallback { GavinsMod.radarConfig.isUseWaypointColor = waypointColors.value }

        alphaTitle = GuiBuilder()
            .setHeight(12)
            .setTopLeft(pos)
            .setHoverable(false)
            .setTitle("gavinsmod.settings.radar.alpha")
            .setTransparency(0.0f)
            .setDrawBorder(false)
            .build()

        pos = pos.add(0f, gapY)


        backgroundAlpha = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.background.alpha")
            .setValue(GavinsMod.radarConfig.backgroundAlpha)
            .setTopLeft(pos)
            .buildSlider()
        backgroundAlpha.setCallback { GavinsMod.radarConfig.backgroundAlpha = backgroundAlpha.value }

        pos = pos.add(0f, gapY)

        pointAlpha = SettingBuilder()
            .setTitle("gavinsmod.settings.radar.point.alpha")
            .setValue(GavinsMod.radarConfig.pointAlpha)
            .setTopLeft(pos)
            .buildSlider()
        pointAlpha.setCallback { GavinsMod.radarConfig.pointAlpha = pointAlpha.value }
        // add all settings to the list
        guis.add(playerEntityColor.gui)
        guis.add(hostileEntityColor.gui)
        guis.add(peacefulEntityColor.gui)
        guis.add(entityItemColor.gui)
        guis.add(waypointColor.gui)
        guis.add(backgroundColor.gui)
        guis.add(scaleSetting.gui)
        guis.add(pointSizeSetting.gui)
        guis.add(peacefulEntityToggle.gui)
        guis.add(hostileEntityToggle.gui)
        guis.add(itemToggle.gui)
        guis.add(waypointToggle.gui)
        guis.add(playerEntityToggle.gui)
        guis.add(waypointColors.gui)
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
        pos = PointF(paddingX, paddingY + gapY)
        playerEntityColor.setPos(pos)
        pos = pos.add(playerEntityColor.getWidth() + padding, 0f)
        hostileEntityColor.setPos(pos)
        pos = PointF(paddingX, pos.y + gapY)
        peacefulEntityColor.setPos(pos)
        pos = pos.add(peacefulEntityColor.getWidth() + padding, 0f)
        entityItemColor.setPos(pos)
        pos = PointF(paddingX, pos.y + gapY)
        waypointColor.setPos(pos)
        pos = pos.add(waypointColor.getWidth() + padding, 0f)
        backgroundColor.setPos(pos)
        pos = PointF(paddingX, pos.y + gapY)
        scaleTitle.position = (pos)
        pos = PointF(paddingX, pos.y + gapY)
        pointSizeSetting.setPos(pos)
        pos = pos.add(pointSizeSetting.getWidth() + padding, 0f)
        scaleSetting.setPos(pos)
        pos = PointF(paddingX, pos.y + gapY)

        toggleTitle.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        playerEntityToggle.setPos(pos)
        pos = pos.add(playerEntityToggle.getWidth() + padding, 0f)
        hostileEntityToggle.setPos(pos)
        pos = PointF(paddingX, pos.y + gapY)
        peacefulEntityToggle.setPos(pos)
        pos = pos.add(peacefulEntityToggle.getWidth() + padding, 0f)
        itemToggle.setPos(pos)
        pos = PointF(paddingX, pos.y + gapY)
        waypointToggle.setPos(pos)
        pos = pos.add(waypointToggle.getWidth() + padding, 0f)
        waypointColors.setPos(pos)

        pos = PointF(paddingX, pos.y + gapY)
        alphaTitle.position = pos
        pos = PointF(paddingX, pos.y + gapY)
        backgroundAlpha.setPos(pos)
        pos = pos.add(backgroundAlpha.getWidth() + padding, 0f)
        pointAlpha.setPos(pos)



        guis.add(0, box)
        guis.add(colorTitle)
        guis.add(alphaTitle)
        guis.add(toggleTitle)
        guis.add(scaleTitle)
        guis.add(alphaTitle)
        updateScaleText(scaleSetting, GavinsMod.radarConfig.scale)
        updateScaleText(pointSizeSetting, GavinsMod.radarConfig.pointSize)
        super.init()
    }

    /**
     * Callback method for the scale setting.
     */
    private fun increaseScale() {
        GavinsMod.radarConfig.scale = GavinsMod.radarConfig.scale + 1
        updateScaleText(scaleSetting, GavinsMod.radarConfig.scale)
    }

    /**
     * Callback for the point size setting.
     */
    private fun togglePointSize() {
        GavinsMod.radarConfig.updatePointSizeCallback()
        updateScaleText(pointSizeSetting, GavinsMod.radarConfig.pointSize)
    }

    private fun updateScaleText(setting: ClickSetting, value: Int) {
        setting.gui.title =
            (Text.translatable(setting.gui.translationKey).append(Text.literal(" (%s)".format(value))))
    }

}