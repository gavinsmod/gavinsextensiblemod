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

package com.peasenet.gui.mod

import com.peasenet.config.commons.TracerEspConfig
import com.peasenet.gavui.*
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.data.ItemEntityFilter
import net.minecraft.text.Text

/**
 *
 * A GUI That allows the player to configure item ESP/Tracers.
 *
 * @param config The [TracerEspConfig] that this GUI will be configuring.
 *
 * @see TracerEspConfig
 * @see ItemEntityFilter
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 01-10-2025
 */
class GuiItemEspTracerConfig(
    private val config: TracerEspConfig<*>
) : GuiElement(Text.translatable("gavinsmod.gui.itemFilterConfig")) {

    private var filterCheckbox: GuiToggle
    private var filterDropdown: GuiScroll
    private var addFilterButton: GuiClick
    private var colorSetting: ColorSetting


    private var box: Gui

    private var padding = 4
    private var startY = 12f
    private var startX = 10f
    private var offsetX = 0
    private var offsetY = 0
    private var screenWidth = 120
    private var screenHeight = 146

    override fun init() {
        guis.clear()
        guis.add(box)
        guis.add(colorSetting.gui)
        guis.add(filterCheckbox)
        guis.add(addFilterButton)
        guis.add(filterDropdown)

        super.init()
    }

    init {
        this.parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2 - screenWidth / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - screenHeight / 2

        box = GuiBuilder().setWidth(screenWidth).setHeight(screenHeight).setHoverable(false).setDrawBorder(true)
            .setTopLeft(offsetX, offsetY).build()

        startX = offsetX.toFloat() + 2f
        startY = offsetY.toFloat() + 2f

        colorSetting =
            SettingBuilder().setTitle("gavinsmod.generic.color").setColor(config.itemColor).setTopLeft(startX, startY)
                .setWidth(screenWidth - padding).buildColorSetting()

        colorSetting.setCallback {
            config.itemColor = colorSetting.color
        }
        startY += 12f
        filterCheckbox =
            GuiBuilder().setTitle("gavinsmod.settings.filter.custom").setHeight(10f).setTopLeft(startX, startY)
                .setWidth(screenWidth - padding).buildToggle()
        startY += 12f
        filterCheckbox.setCallback {
            config.useItemEspFilter = filterCheckbox.isOn
            filterCheckbox.setState(config.useItemEspFilter)
        }
        filterCheckbox.setState(config.useItemEspFilter)
        addFilterButton =
            GuiBuilder().setTitle("gavinsmod.settings.filter.add").setTopLeft(startX, startY).setHeight(10f)
                .setWidth(screenWidth - padding).setCallback {
                    minecraftClient.setScreen(
                        GuiItemFilter(
                            this, ItemEntityFilter(
                                "A Cool Name",
                                "A cool item name",
                            ), config
                        )
                    )
                }
                .setSymbol('+')
                .buildClick()
        startY += 12f
        var filterChildren: ArrayList<Gui> = ArrayList()
        for (filter in config.itemFilterList) {
            val guiClick = GuiBuilder().setTitle(filter.filterName).setHeight(10f).setWidth(100f)
                .setBackgroundColor(if (filter.enabled) GavUI.enabledColor() else GavUI.backgroundColor())
                .setCallback { minecraftClient.setScreen(GuiItemFilter(this, filter, config)) }.buildClick()
            filterChildren.add(guiClick)
        }
        filterDropdown =
            GuiBuilder().setTitle("gavinsmod.settings.filters").setTopLeft(startX, startY).setMaxChildren(8)
                .setIsParent(true)
                .setDraggable(false)
                .setWidth(screenWidth - padding).setChildren(filterChildren).buildScroll()
    }
}