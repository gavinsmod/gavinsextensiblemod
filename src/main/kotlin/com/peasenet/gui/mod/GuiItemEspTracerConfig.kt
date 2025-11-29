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
import com.peasenet.gavui.GavUI
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.settings.*
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
    private val config: TracerEspConfig<*>,
) : GuiElement(Text.translatable("gavinsmod.gui.itemFilterConfig")) {

    private var filterCheckbox: ToggleSetting
    private var filterDropdown: GuiScroll
    private var addFilterButton: ClickSetting
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
        guis.add(filterCheckbox.gui)
        guis.add(addFilterButton.gui)
        guis.add(filterDropdown)

        super.init()
    }

    init {
        this.parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2 - screenWidth / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - screenHeight / 2

        box = GuiBuilder<Gui>().setWidth(screenWidth).setHeight(screenHeight).setHoverable(false).setDrawBorder(true)
            .setTopLeft(offsetX, offsetY).build()

        startX = offsetX.toFloat() + 2f
        startY = offsetY.toFloat() + 2f
        colorSetting = colorSetting {
            title = "gavinsmod.generic.color"
            color = config.itemColor
            topLeft = PointF(startX, startY)
            width = (screenWidth - padding).toFloat()
            callback = { config.itemColor = it.color }
        }
        startY += 12f

        filterCheckbox = toggleSetting {
            title = "gavinsmod.settings.filter.custom"
            topLeft = PointF(startX, startY)
            width = (screenWidth - padding).toFloat()
            callback = { config.useItemEspFilter = it.state }
            state = config.useItemEspFilter
        }
        startY += 12f
        addFilterButton = clickSetting {
            title = "gavinsmod.settings.filter.add"
            topLeft = PointF(startX, startY)
            width = (screenWidth - padding).toFloat()
            callback = {
                minecraftClient.setScreen(
                    GuiItemFilter(
                        this@GuiItemEspTracerConfig, ItemEntityFilter(
                            "A Cool Name",
                            "A cool item name",
                        ), config
                    )
                )
            }
            symbol = "+"
        }
        startY += 12f
        val filterChildren: ArrayList<Gui> = ArrayList()
        for (filter in config.itemFilterList) {
            val guiClick = clickSetting {
                title = filter.filterName
                width = 100f
                color = if (filter.enabled) GavUI.enabledColor() else GavUI.backgroundColor()
                callback = { minecraftClient.setScreen(GuiItemFilter(this@GuiItemEspTracerConfig, filter, config)) }
            }
            filterChildren.add(guiClick.gui)
        }
        filterDropdown =
            GuiBuilder<GuiScroll>().setTitle("gavinsmod.settings.filters").setTopLeft(startX, startY).setMaxChildren(8)
                .setIsParent(true)
                .setDraggable(false)
                .setWidth(screenWidth - padding).setChildren(filterChildren).buildScroll()
    }
}