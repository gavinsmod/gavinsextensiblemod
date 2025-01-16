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
package com.peasenet.gui.mod.waypoint

import com.peasenet.config.WaypointConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.GavinsModClient.Companion.player
import com.peasenet.main.Mods.Companion.getMod
import com.peasenet.main.Settings
import com.peasenet.mods.render.waypoints.Waypoint
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.settings.ToggleSetting
import com.peasenet.util.Dimension
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3i
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.floor

/**
 * A gui that allows the user to add, delete, or modify a waypoint.
 *
 * @param w - The waypoint to modify. Defaults to a new waypoint.
 *
 * @author GT3CH1
 * @version 01-14-2025
 * @since 04-11-2023
 */
class GuiWaypoint(private var w: Waypoint = Waypoint()) :
    GuiElement(Text.translatable("gavinsmod.mod.render.waypoints")) {

    /**
     * The text field used to name the waypoint.
     */
    private lateinit var textField: TextFieldWidget

    /**
     * The x coordinate text field.
     */
    private lateinit var xCoordinate: TextFieldWidget

    /**
     * The y coordinate text field.
     */
    private lateinit var yCoordinate: TextFieldWidget

    /**
     * The z coordinate text field.
     */
    private lateinit var zCoordinate: TextFieldWidget

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

    /**
     * The button used to toggle the waypoint's visibility.
     */
    private lateinit var waypointToggle: ToggleSetting

    /**
     * The button used to toggle the waypoint's esp.
     */
    private lateinit var espToggle: ToggleSetting

    /**
     * The tracer toggle setting.
     */
    private lateinit var tracerToggle: ToggleSetting

    /**
     * The overlord toggle setting.
     */
    private lateinit var overworldToggle: ToggleSetting

    /**
     * The nether toggle setting.
     */
    private lateinit var netherToggle: ToggleSetting

    /**
     * The end dimension toggle setting.
     */
    private lateinit var endToggle: ToggleSetting

    /**
     * The button that is used to change the waypoints color.
     */
    private lateinit var colorCycle: ColorSetting

    /**
     * The background box.
     */
    private lateinit var box: Gui

    /**
     * The width of the gui.
     */
    private var width = 145

    /**
     * The height of the gui.
     */
    private var height = 154

    /**
     * The padding of each element.
     */
    private var padding = 5

    /**
     * The offset and padding in the x and y planes.
     */
    private var offsetX = 0

    /**
     * The offset in the y plane.
     */
    private var offsetY = 0

    /**
     * The padding in the x plane.
     */
    private var paddingX = 0

    /**
     * The padding in the y plane.
     */
    private var paddingY = 0


    init {
        setup()
    }

    /**
     * Gets the floored player position as a Vec3i.
     */
    private fun flooredPlayerPos(): Vec3i {
        val playerPos = player!!.getPos()
        return Vec3i(floor(playerPos.x).toInt(), floor(playerPos.y).toInt() + 1, floor(playerPos.z).toInt())
    }

    /**
     * Callback used for saving the waypoint after editing.
     */
    private fun saveCallback() {
        w.clearDimensions()
        if (overworldToggle.value) w.addDimension(Dimension.OVERWORLD)
        if (netherToggle.value) w.addDimension(Dimension.NETHER)
        if (endToggle.value) w.addDimension(Dimension.END)
        val newWaypoint = Waypoint(
            Vec3i(xCoordinate.text.toInt(), yCoordinate.text.toInt(), zCoordinate.text.toInt()),
            textField.text,
            w.dimensions,
            colorCycle.color,
            waypointToggle.value,
            espToggle.value,
            tracerToggle.value,
            w.uuid,
        )
        getConfig().addWaypoint(newWaypoint)
        getMod("waypoints")?.reloadSettings()
        GavinsMod.guiSettings.reloadGui()
        parent = GavinsMod.guiSettings
        close()
    }

    /**
     * Performs initial GUI setup, such as setting the states for each element.
     */
    private fun setup() {

        val buttonWidth = 42
        val wholeButtonWidth = buttonWidth * 3 + padding * 2

        parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding

        box = GuiBuilder()
            .setTopLeft(offsetX, offsetY)
            .setWidth(width)
            .setHeight(height)
            .setTitle(Text.literal(""))
            .build()
        textField =
            TextFieldWidget(minecraftClient.textRenderer, offsetX + 40, offsetY + 10, 100, 10, Text.literal(w.name))
        textField.text = w.name
        focused = textField


        offsetY += 20 + padding
        colorCycle = SettingBuilder()
            .setTitle("gavinsmod.settings.render.waypoints.color")
            .setColor(w.color)
            .setTopLeft(PointF(paddingX, offsetY))
            .setWidth(wholeButtonWidth)
            .setHoverable(true)
            .buildColorSetting()
        offsetY += 14
        waypointToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.enabled")
            .setTopLeft(PointF(paddingX, offsetY))
            .setWidth(wholeButtonWidth)
            .setState(w.isEnabled)
            .setHoverable(true)
            .buildToggleSetting()
        offsetY += 14
        espToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.esp")
            .setTopLeft(paddingX.toFloat(), offsetY.toFloat())
            .setWidth((wholeButtonWidth / 2 - padding / 2).toFloat())
            .setHoverable(true)
            .setState(w.renderEsp)
            .buildToggleSetting()

        tracerToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.tracer")
            .setTopLeft(PointF((offsetX + padding + padding / 2 + wholeButtonWidth / 2).toFloat(), (offsetY).toFloat()))
            .setWidth((wholeButtonWidth / 2 - padding / 2).toFloat())
            .setHoverable(true)
            .setState(w.renderTracer)
            .buildToggleSetting()
        offsetY += 28
        overworldToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.overworld")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth(wholeButtonWidth.toFloat())
            .setHoverable(true)
            .setState(w.hasDimension(Dimension.OVERWORLD))
            .buildToggleSetting()
        offsetY += 14
        netherToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.nether")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth(wholeButtonWidth.toFloat())
            .setHoverable(true)
            .setState(w.hasDimension(Dimension.NETHER))
            .buildToggleSetting()
        offsetY += 14
        endToggle = SettingBuilder()
            .setTitle("gavinsmod.settings.end")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth(wholeButtonWidth.toFloat())
            .setHoverable(true)
            .setState(w.hasDimension(Dimension.END))
            .buildToggleSetting()
        offsetY += 14
        xCoordinate = TextFieldWidget(minecraftClient.textRenderer, paddingX + 11, offsetY, 30, 10, Text.literal(""))
        yCoordinate = TextFieldWidget(minecraftClient.textRenderer, paddingX + 56, offsetY, 30, 10, Text.literal(""))
        zCoordinate = TextFieldWidget(minecraftClient.textRenderer, paddingX + 104, offsetY, 30, 10, Text.literal(""))
        var coordinates = w.coordinates
        if (w.name.isEmpty()) coordinates = flooredPlayerPos()
        xCoordinate.text = coordinates.x.toString()
        yCoordinate.text = coordinates.y.toString()
        zCoordinate.text = coordinates.z.toString()
        xCoordinate.setTextPredicate(checkIfSignedInt())
        yCoordinate.setTextPredicate(checkIfSignedInt())
        zCoordinate.setTextPredicate(checkIfSignedInt())
        addSelectableChild(textField)
        addSelectableChild(xCoordinate)
        addSelectableChild(yCoordinate)
        addSelectableChild(zCoordinate)
        offsetY += 14
        saveSettings =
            GuiBuilder()
                .setTitle("gavinsmod.settings.save")
                .setTopLeft(paddingX, offsetY)
                .setWidth(buttonWidth)
                .setBackgroundColor(Colors.GREEN).setCallback {
                    saveCallback()
                }
                .setHoverable(true)
                .buildClick()

        cancelSettings = GuiBuilder()
            .setTitle("gavinsmod.settings.cancel")
            .setTopLeft((paddingX + padding + buttonWidth), offsetY)
            .setWidth(buttonWidth)
            .setBackgroundColor(Colors.YELLOW)
            .setHoverable(true)
            .setCallback {
                client!!.setScreen(parent)
            }
            .buildClick()

        deleteSettings = GuiBuilder()
            .setTitle("gavinsmod.settings.delete")
            .setTopLeft(paddingX + padding * 2 + buttonWidth * 2, offsetY)
            .setWidth(buttonWidth.toFloat())
            .setBackgroundColor(Colors.RED)
            .setHoverable(true)
            .setCallback {
                deleteCallback()
            }
            .buildClick()
        box.isHoverable = false

        guis.add(saveSettings)
        guis.add(cancelSettings)
        guis.add(deleteSettings)
        guis.add(waypointToggle.gui)
        guis.add(espToggle.gui)
        guis.add(tracerToggle.gui)
        guis.add(overworldToggle.gui)
        guis.add(netherToggle.gui)
        guis.add(endToggle.gui)
        guis.add(colorCycle.gui)
    }

    /**
     * Callback used when the delete button is clicked. This removes a waypoint from the configuration and then reloads the gui.
     */
    private fun deleteCallback() {
        getConfig().removeWaypoint(w)
        getMod("waypoints")?.reloadSettings()
        GavinsMod.guiSettings.reloadGui()
        close()
    }

    /**
     * A predicate that checks if the given string is a signed integer.
     */
    private fun checkIfSignedInt(): Predicate<String> {
        return Predicate { s: String ->
            if (s.isEmpty()) return@Predicate true
            if (s == "-" || s == ".") return@Predicate true
            // match any signed integer within Integer.MIN_VALUE and Integer.MAX_VALUE
            val regex = "^-?[0-9]+$"
            if (s.matches(regex.toRegex())) {
                try {
                    s.toInt()
                    return@Predicate true
                } catch (e: Exception) {
                    return@Predicate false
                }
            }
            false
        }
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        box.render(drawContext, client!!.textRenderer, mouseX, mouseY, delta)
        super.render(drawContext, mouseX, mouseY, delta)
        guis.forEach(Consumer { obj: Gui -> obj.show() })
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Name: "),
            paddingX,
            (textField.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("X:"),
            (paddingX + 1),
            (xCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Y:"),
            (paddingX + 46),
            (yCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Z:"),
            (paddingX + 94),
            (zCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Dimensions"),
            (paddingX + 1),
            (espToggle.gui.y + 15).toInt(),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        textField.render(drawContext, mouseX, mouseY, delta)
        xCoordinate.render(drawContext, mouseX, mouseY, delta)
        yCoordinate.render(drawContext, mouseX, mouseY, delta)
        zCoordinate.render(drawContext, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for (child in guis) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                textField.isFocused = false
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    private companion object {
        fun getConfig(): WaypointConfig = Settings.getConfig("waypoints")
    }
}