package com.peasenet.gui.mod

import com.peasenet.config.TracerEspConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.ToggleSetting
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
    private lateinit var saveSettings: GuiClick

    /**
     * The button used to cancel the waypoint.
     */
    private lateinit var cancelSettings: GuiClick

    /**
     * The button used to delete the waypoint.
     */
    private lateinit var deleteSettings: GuiClick

    private lateinit var searchLore: ToggleSetting
    private lateinit var searchName: ToggleSetting

    private fun setup() {
        parent = parentScreen
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding
        var elementWidth = width - padding * 2
        box = GuiBuilder().setTopLeft(PointF(offsetX.toFloat(), offsetY.toFloat())).setWidth(width.toFloat())
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
//        focused = filterName
        filterName.text = itemEspFilter.filterName
        filterString.text = itemEspFilter.filterString

        addSelectableChild(filterName)
        addSelectableChild(filterString)
        offsetY += 14
        filterEnable =
            SettingBuilder().setTitle("gavinsmod.settings.enabled").setTopLeft(PointF(paddingX, offsetY))
                .setWidth(elementWidth)
                .setHoverable(true).buildToggleSetting()
        filterEnable.value = itemEspFilter.enabled
        filterEnable.gui.width = elementWidth.toFloat()
        offsetY += 12
        searchLore =
            SettingBuilder().setTitle("gavinsmod.settings.searchLore")
                .setTopLeft(PointF(paddingX, offsetY))
                .setHoverable(true).buildToggleSetting()
        searchLore.value = itemEspFilter.searchLore
        searchLore.gui.width = elementWidth.toFloat()
        offsetY += 12
        searchName =
            SettingBuilder().setTitle("gavinsmod.settings.searchName").setTopLeft(PointF(paddingX, offsetY))
                .setHoverable(true)
                .buildToggleSetting()
        searchName.value = itemEspFilter.searchCustomName
        searchName.gui.width = elementWidth.toFloat()
        offsetY += 12
        // save, cancel, delete
        var threeButtonWidth = (elementWidth / 3).toFloat() + (-padding * 0.5f)
        saveSettings =
            GuiBuilder().setTopLeft(PointF(paddingX, offsetY)).setTitle("gavinsmod.settings.save")
                .setWidth(threeButtonWidth)
                .setHeight(10f).setBackgroundColor(Colors.GREEN).buildClick()
        saveSettings.setCallback {
            val tmpFilter = ItemEntityFilter(
                filterName.text,
                filterString.text,
                searchName.value,
                searchLore.value,
                filterEnable.value,
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
                return@setCallback
            }
            itemEspFilter.filterName = filterName.text
            itemEspFilter.filterString = filterString.text
            itemEspFilter.enabled = filterEnable.value
            itemEspFilter.searchCustomName = searchName.value
            itemEspFilter.searchLore = searchLore.value

            config.itemFilterList.removeIf { it.uuid == itemEspFilter.uuid }
            config.itemFilterList.add(itemEspFilter)
            config.saveConfig()
            parent = GuiItemEspTracerConfig(config)
            minecraftClient.setScreen(parent)
        }

        cancelSettings =
            GuiBuilder().setTopLeft(PointF(paddingX + threeButtonWidth + padding, offsetY.toFloat()))
                .setTitle("gavinsmod.settings.cancel")
                .setWidth(threeButtonWidth).setHeight(10f).setBackgroundColor(Colors.YELLOW).setCallback {
                    minecraftClient.setScreen(parent)
                }.buildClick()
        deleteSettings =
            GuiBuilder().setTopLeft(PointF(paddingX + (threeButtonWidth + padding) * 2, offsetY.toFloat()))
                .setTitle("gavinsmod.settings.delete")
                .setWidth(threeButtonWidth).setHeight(10f).setBackgroundColor(Colors.RED).setCallback {
                    config.itemFilterList.removeIf { it.uuid == itemEspFilter.uuid }
                    config.saveConfig()
                    parent = GuiItemEspTracerConfig(config)
                    minecraftClient.setScreen(parent)
                }.buildClick()
        guis.add(filterEnable.gui)
        guis.add(saveSettings)
        guis.add(cancelSettings)
        guis.add(deleteSettings)
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