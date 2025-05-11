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
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.settings.ClickSetting
import com.peasenet.settings.ToggleSetting
import com.peasenet.settings.clickSetting
import com.peasenet.settings.toggleSetting
import com.peasenet.util.PlayerUtils
import com.peasenet.util.data.ItemEntityFilter
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text

/**
 *
 * A GUI that allows the player to configure item filters for ESP/Tracers.
 *
 * @param parentScreen The parent screen that this GUI will be returning to.
 * @param filter The [ItemEntityFilter] that this GUI will be configuring.
 * @param config The [TracerEspConfig] that this GUI will be configuring.
 *
 * @see ItemEntityFilter
 * @see TracerEspConfig
 *
 * @sample GuiItemEspTracerConfig
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 01-10-2025
 */
class GuiItemFilter(private val parentScreen: Screen, filter: ItemEntityFilter, val config: TracerEspConfig<*>) :
    GuiElement(Text.of(filter.filterName)) {
    /**
     * The width of the gui.
     */
    var width = 152

    /**
     * The height of the gui.
     */
    var height = 98

    /**
     * The offset and padding in the x and y planes.
     */
    private var offsetX = 0
    private var paddingX = 0
    private var offsetY = 0
    private var paddingY = 0
    private var padding = 2
    private var box: Gui? = null

    private var itemEspFilter: ItemEntityFilter = filter
    private lateinit var filterEnable: ToggleSetting
    private lateinit var filterString: TextFieldWidget
    private lateinit var filterName: TextFieldWidget

    /**
     * The button used to save the waypoint.
     */
    private lateinit var saveSettings: ClickSetting

    /**
     * The button used to cancel the waypoint.
     */
    private lateinit var cancelSettings: ClickSetting

    /**
     * The button used to delete the waypoint.
     */
    private lateinit var deleteSettings: ClickSetting

    private lateinit var searchLore: ToggleSetting
    private lateinit var searchName: ToggleSetting

    private fun setup() {
        parent = parentScreen
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding
        val elementWidth = width - padding * 2
        box = GuiBuilder<Gui>().setTopLeft(PointF(offsetX.toFloat(), offsetY.toFloat())).setWidth(width.toFloat())
            .setHeight(height.toFloat()).setTitle(Text.literal("")).setHoverable(false).build()

        offsetY += 12
        val filteredString = itemEspFilter.filterString
        filterName = TextFieldWidget(
            minecraftClient.textRenderer,
            offsetX + padding,
            offsetY,
            elementWidth,
            12,
            Text.literal(itemEspFilter.filterName)
        )
        offsetY += 24
        filterString = TextFieldWidget(
            minecraftClient.textRenderer, offsetX + padding, offsetY, elementWidth, 12, Text.literal(filteredString)
        )
        filterName.text = itemEspFilter.filterName
        filterString.text = itemEspFilter.filterString

        addSelectableChild(filterName)
        addSelectableChild(filterString)
        offsetY += 14
        filterEnable = toggleSetting {
            title = "gavinsmod.settings.enabled"
            topLeft = PointF(paddingX, offsetY)
            width = elementWidth.toFloat()
            hoverable = true
            state = itemEspFilter.enabled
        }
        offsetY += 12
        searchLore = toggleSetting {
            title = "gavinsmod.settings.searchLore"
            topLeft = PointF(paddingX, offsetY)
            hoverable = true
            state = itemEspFilter.searchLore
            width = elementWidth.toFloat()
        }
        offsetY += 12
        searchName = toggleSetting {
            title = "gavinsmod.settings.searchName"
            topLeft = PointF(paddingX, offsetY)
            hoverable = true
            state = itemEspFilter.searchCustomName
            width = elementWidth.toFloat()
        }
        searchName.gui.width = elementWidth.toFloat()
        offsetY += 12
        // save, cancel, delete
        val threeButtonWidth = (elementWidth / 3).toFloat() + (-padding * 0.5f)
        saveSettings = clickSetting {
            title = "gavinsmod.settings.save"
            topLeft = PointF(paddingX, offsetY)
            width = threeButtonWidth
            height = 10f
            color = Colors.GREEN
            callback = {
                val tmpFilter = ItemEntityFilter(
                    filterName.text,
                    filterString.text,
                    searchName.state,
                    searchLore.state,
                    filterEnable.state,
                    itemEspFilter.uuid
                )
                if (!ItemEntityFilter.validate(tmpFilter)) {
                    PlayerUtils.sendMessage(
                        Text.translatable("gavinsmod.settings.filter.invalid"),
                        true
                    )
                    config.itemFilterList.removeIf { it.uuid == tmpFilter.uuid }
                    config.itemFilterList.add(tmpFilter)
                    config.saveConfig()
                    parent = GuiItemEspTracerConfig(config)
                    minecraftClient.setScreen(parent)
                } else {
                    itemEspFilter.filterName = filterName.text
                    itemEspFilter.filterString = filterString.text
                    itemEspFilter.enabled = filterEnable.state
                    itemEspFilter.searchCustomName = searchName.state
                    itemEspFilter.searchLore = searchLore.state

                    config.itemFilterList.removeIf { it.uuid == itemEspFilter.uuid }
                    config.itemFilterList.add(itemEspFilter)
                    config.saveConfig()
                    parent = GuiItemEspTracerConfig(config)
                    minecraftClient.setScreen(parent)
                }
            }
        }
//        cancelSettings =
//            SettingBuilder<ClickSetting>().setTopLeft(
//                PointF(
//                    paddingX + threeButtonWidth + padding,
//                    offsetY.toFloat()
//                )
//            )
//                .setTitle("gavinsmod.settings.cancel")
//                .setWidth(threeButtonWidth).setHeight(10f).setBackgroundColor(Colors.YELLOW).setCallback {
//                    minecraftClient.setScreen(parent)
//                }.buildClickSetting()
        cancelSettings = clickSetting {
            title = "gavinsmod.settings.cancel"
            topLeft = PointF(paddingX + threeButtonWidth + padding, offsetY.toFloat())
            width = threeButtonWidth
            height = 10f
            color = Colors.YELLOW
            callback = { minecraftClient.setScreen(parent) }
        }
        deleteSettings = clickSetting {
            title = "gavinsmod.settings.delete"
            topLeft = PointF(paddingX + (threeButtonWidth + padding) * 2, offsetY.toFloat())
            width = threeButtonWidth
            height = 10f
            color = Colors.RED
            callback = {
                config.itemFilterList.removeIf { it.uuid == itemEspFilter.uuid }
                config.saveConfig()
                parent = GuiItemEspTracerConfig(config)
                minecraftClient.setScreen(parent)
            }
        }
        guis.add(filterEnable.gui)
        guis.add(saveSettings.gui)
        guis.add(cancelSettings.gui)
        guis.add(deleteSettings.gui)
        guis.add(searchLore.gui)
        guis.add(searchName.gui)
    }

    init {
        setup()
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        val textRenderer = client!!.textRenderer
        box!!.render(drawContext, textRenderer, mouseX, mouseY, delta)

        super.render(drawContext, mouseX, mouseY, delta)
        offsetY += 2
        drawContext.drawText(
            textRenderer,
            Text.translatable("gavinsmod.settings.name"),
            offsetX + padding,
            offsetY,
            GavUISettings.getColor("gui.color.foreground").asInt,
            false,
        )
        offsetY += 24
        drawContext.drawText(
            textRenderer,
            Text.translatable("gavinsmod.settings.filter"),
            offsetX + padding,
            offsetY,
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        offsetY = minecraftClient.window.scaledHeight - 12
        drawContext.drawText(
            textRenderer,
            Text.of("Filter UUID: ${itemEspFilter.uuid}"),
            10,
            offsetY,
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        filterName.render(drawContext, mouseX, mouseY, delta)
        filterString.render(drawContext, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for (child in guis) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                filterName.isFocused = false
                filterString.isFocused = false
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }
}