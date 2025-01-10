package com.peasenet.gui.mod.esp.item

import com.peasenet.config.EspConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiScroll
import com.peasenet.gavui.GuiToggle
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Settings
import com.peasenet.mods.esp.ItemEspFilter
import net.minecraft.text.Text

/**
 *
 * @author GT3CH1
 * @version 01-10-2025
 * @since 01-10-2025
 */
class GuiItemEsp() : GuiElement(Text.of("ITEM_ESP")) {
    private lateinit var filterCheckbox: GuiToggle
    private lateinit var filterDropdown: GuiScroll
    private lateinit var addFilterButton: GuiClick
    override fun init() {
        filterCheckbox = GuiBuilder().setTitle("ITEM_ESP").setHeight(10f).setTopLeft(10f, 12f).buildToggle()
        val cfg = Settings.getConfig<EspConfig>("esp")
        filterCheckbox.setCallback {
            cfg.useItemEspFilter = filterCheckbox.isOn
            filterCheckbox.setState(cfg.useItemEspFilter)
        }
        filterCheckbox.setState(cfg.useItemEspFilter)

        addFilterButton = GuiBuilder().setTitle("ADD_FILTER").setTopLeft(10f, 24f).setHeight(10f).setWidth(100f)
            .setCallback {
                GavinsModClient.minecraftClient.setScreen(
                    GuiItemFilter(
                        this,
                        ItemEspFilter(
                            "Filter String",
                            "Filter Name",
                        )
                    )
                )
            }.buildClick()

        var filterChildren: ArrayList<Gui> = ArrayList()
        for (filter in cfg.itemEspFilterList) {
            val guiClick = GuiBuilder().setTitle(filter.filterName).setHeight(10f)
                .setWidth(100f)/*TODO: Create sub-gui for settings on this filter. .setCallback {} */
                .setCallback { GavinsModClient.minecraftClient.setScreen(GuiItemFilter(this, filter)) }.buildClick()
            filterChildren.add(guiClick)
        }
        filterDropdown =
            GuiBuilder().setTitle("ITEM_ESP_LIST").setTopLeft(10f, 36f).setMaxChildren(10).setChildren(filterChildren)
                .buildScroll()
        guis.add(filterCheckbox)
        guis.add(addFilterButton)
        guis.add(filterDropdown)

        super.init()
    }
}