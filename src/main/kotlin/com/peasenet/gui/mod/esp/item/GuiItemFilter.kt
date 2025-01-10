package com.peasenet.gui.mod.esp.item

import com.peasenet.config.EspConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.Settings
import com.peasenet.mods.esp.ItemEspFilter
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.ToggleSetting
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text

/**
 *
 * @author GT3CH1
 * @version 01-10-2025
 * @since 01-10-2025
 */
class GuiItemFilter(private val parentScreen: Screen, filter: ItemEspFilter) :
    GuiElement(Text.of(filter.filterString)) {
    /**
     * The width of the gui.
     */
    var width = 145

    /**
     * The height of the gui.
     */
    var height = 135

    /**
     * The offset and padding in the x and y planes.
     */
    private var offsetX = 0
    private var paddingX = 0
    private var offsetY = 0
    private var paddingY = 0
    private var padding = 5
    private var box: Gui? = null

    private var itemEspFilter: ItemEspFilter = filter
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

    private fun setup() {
        parent = parentScreen
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding
        box = GuiBuilder().setTopLeft(PointF(offsetX.toFloat(), offsetY.toFloat())).setWidth(width.toFloat())
            .setHeight(height.toFloat()).setTitle(Text.literal("")).setHoverable(false).build()
        filterName = TextFieldWidget(
            minecraftClient.textRenderer, offsetX + padding, offsetY + 24, 130, 12, Text.of("")
        )
        var filteredString = itemEspFilter.filterString
        filterString = TextFieldWidget(
            minecraftClient.textRenderer, offsetX + padding, offsetY + 60, 130, 12, Text.literal(filteredString)
        )
        focused = filterName
        filterString.text = filteredString
        itemEspFilter.filterName.also { filterName.text = it }

        addSelectableChild(filterName)
        addSelectableChild(filterString)

        filterEnable = SettingBuilder()
            .setTitle("gavinsmod.settings.enabled")
            .setTopLeft(PointF(paddingX, offsetY + 80))
            .setHoverable(true)
            .buildToggleSetting()
        filterEnable.value = itemEspFilter.enabled
        filterEnable.gui.width = 130.toFloat()

        // save, cancel, delete
        saveSettings = GuiBuilder().setTopLeft(PointF(paddingX, offsetY + 120)).setTitle("gavinsmod.settings.save")
            .setWidth(40f).setHeight(10f).setBackgroundColor(Colors.GREEN).buildClick()

        saveSettings.setCallback {
            val previousName = itemEspFilter.filterString
            itemEspFilter.filterString = filterString.text
            itemEspFilter.filterName = filterName.text
            itemEspFilter.enabled = filterEnable.value
            val cfg = Settings.getConfig<EspConfig>("esp")
            cfg.itemEspFilterList.removeIf { it.filterString == previousName }
            cfg.itemEspFilterList.add(itemEspFilter)
            cfg.saveConfig()
            minecraftClient.setScreen(parent)
        }

        cancelSettings =
            GuiBuilder().setTopLeft(PointF(paddingX + 45, offsetY + 120)).setTitle("gavinsmod.settings.cancel")
                .setWidth(40f).setHeight(10f).setBackgroundColor(Colors.YELLOW)
                .setCallback {
                    minecraftClient.setScreen(parent)
                }
                .buildClick()
        deleteSettings =
            GuiBuilder().setTopLeft(PointF(paddingX + 90, offsetY + 120)).setTitle("gavinsmod.settings.delete")
                .setWidth(40f).setHeight(10f).setBackgroundColor(Colors.RED)
                .setCallback {
                    val cfg = Settings.getConfig<EspConfig>("esp")
                    cfg.itemEspFilterList.removeIf { it.filterString == itemEspFilter.filterString }
                    cfg.saveConfig()
                    minecraftClient.setScreen(parent)
                }
                .buildClick()
        guis.add(filterEnable.gui)
        guis.add(saveSettings)
        guis.add(cancelSettings)
        guis.add(deleteSettings)
    }

    init {
        setup()
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        val textRenderer = client!!.textRenderer
        box!!.render(drawContext, textRenderer, mouseX, mouseY, delta)
        offsetY += 12
        drawContext.drawText(
            textRenderer,
            Text.of("Filter Name"),
            offsetX + padding,
            offsetY,
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        offsetY += 36
        drawContext.drawText(
            textRenderer,
            Text.of("Filter String"),
            offsetX + padding,
            offsetY,
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        filterName.render(drawContext, mouseX, mouseY, delta)
        filterString.render(drawContext, mouseX, mouseY, delta)
        super.render(drawContext, mouseX, mouseY, delta)
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