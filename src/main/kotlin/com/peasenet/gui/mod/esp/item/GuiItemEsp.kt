package com.peasenet.gui.mod.esp.item

import com.peasenet.config.EspConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Settings
import com.peasenet.mods.esp.ItemEspFilter
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import net.minecraft.text.Text

/**
 *
 * @author GT3CH1
 * @version 01-10-2025
 * @since 01-10-2025
 */
class GuiItemEsp() :
    GuiElement(Text.translatable("gavinsmod.mod.esp.item")) {

    private var espConfig = Settings.getConfig<EspConfig>("esp")

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

        box = GuiBuilder().setWidth(screenWidth).setHeight(screenHeight).setHoverable(false)
            .setDrawBorder(true)
            .setTopLeft(offsetX, offsetY)
            .build()

        startX = offsetX.toFloat() + 2f
        startY = offsetY.toFloat() + 2f

        colorSetting = SettingBuilder()
            .setTitle("gavinsmod.settings.esp.item.color")
            .setColor(espConfig.itemColor)
            .setTopLeft(startX, startY)
            .setWidth(screenWidth - padding)
            .buildColorSetting()

        colorSetting.setCallback {
            espConfig.itemColor = colorSetting.color
        }
        startY += 12f
        filterCheckbox =
            GuiBuilder()
                .setTitle("gavinsmod.settings.esp.item.filtering")
                .setHeight(10f)
                .setTopLeft(startX, startY)
                .setWidth(screenWidth - padding)
                .buildToggle()
        startY += 12f
        val cfg = Settings.getConfig<EspConfig>("esp")
        filterCheckbox.setCallback {
            cfg.useItemEspFilter = filterCheckbox.isOn
            filterCheckbox.setState(cfg.useItemEspFilter)
        }
        filterCheckbox.setState(cfg.useItemEspFilter)
        addFilterButton = GuiBuilder()
            .setTitle("gavinsmod.settings.esp.item.addFilter")
            .setTopLeft(startX, startY)
            .setHeight(10f)
            .setWidth(screenWidth - padding)
            .setCallback {
                minecraftClient.setScreen(
                    GuiItemFilter(
                        this, ItemEspFilter(
                            "Filter String",
                            "Filter Name",
                        )
                    )
                )
            }.buildClick()
        startY += 12f
        var filterChildren: ArrayList<Gui> = ArrayList()
        for (filter in cfg.itemEspFilterList) {
            val guiClick = GuiBuilder().setTitle(filter.filterName).setHeight(10f)
                .setWidth(100f)
                .setCallback { minecraftClient.setScreen(GuiItemFilter(this, filter)) }.buildClick()
            filterChildren.add(guiClick)
        }
        filterDropdown = GuiBuilder()
            .setTitle("gavinsmod.settings.esp.item.filterList")
            .setTopLeft(startX, startY)
            .setMaxChildren(8)
            .setWidth(screenWidth - padding)
            .setChildren(filterChildren).buildScroll()
    }
}