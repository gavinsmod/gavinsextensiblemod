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
package com.peasenet.gui.mod.waypoint

import com.peasenet.config.waypoint.WaypointConfig
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
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
import com.peasenet.settings.*
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
    private lateinit var saveSettings: ClickSetting

    /**
     * The button used to cancel the waypoint.
     */
    private lateinit var cancelSettings: ClickSetting

    /**
     * The button used to delete the waypoint.
     */
    private lateinit var deleteSettings: ClickSetting

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
     * The padding of each element.
     */
    private var padding = 5.0f

    /**
     * The offset and padding in the x and y planes.
     */
    private var offsetX = 0.0f

    /**
     * The offset in the y plane.
     */
    private var offsetY = 0.0f

    /**
     * The padding in the x plane.
     */
    private var paddingX = 0.0f

    /**
     * The padding in the y plane.
     */
    private var paddingY = 0.0f


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
        if (overworldToggle.state) w.addDimension(Dimension.OVERWORLD)
        if (netherToggle.state) w.addDimension(Dimension.NETHER)
        if (endToggle.state) w.addDimension(Dimension.END)
        val newWaypoint = Waypoint(
            Vec3i(xCoordinate.text.toInt(), yCoordinate.text.toInt(), zCoordinate.text.toInt()),
            textField.text,
            w.dimensions,
            colorCycle.color,
            waypointToggle.state,
            espToggle.state,
            tracerToggle.state,
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
        width = 148
        height = 160
        val buttonWidth = 42
        val wholeButtonWidth = buttonWidth * 3 + padding * 2

        parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2.0f - width / 2.0f
        offsetY = minecraftClient.window.scaledHeight / 2.0f - height / 2.0f
        paddingX = offsetX + padding
        paddingY = offsetY + padding

        box = GuiBuilder<Gui>()
            .setTopLeft(offsetX, offsetY)
            .setWidth(width)
            .setHeight(height)
            .setTitle(Text.literal(""))
            .build()
        textField =
            TextFieldWidget(minecraftClient.textRenderer, (offsetX + 40f).toInt(),
                (offsetY + 10f).toInt(), 100, 10, Text.literal(w.name))
        textField.text = w.name
        focused = textField


        offsetY += 20 + padding
        colorCycle = colorSetting {
            title = "gavinsmod.settings.render.waypoints.color"
            color = w.color
            topLeft = PointF(paddingX.toFloat(), offsetY.toFloat())
            width = wholeButtonWidth.toFloat()
        }
        offsetY += 14

        waypointToggle = toggleSetting {
            title = "gavinsmod.settings.enabled"
            topLeft = PointF(paddingX.toFloat(), offsetY.toFloat())
            width = wholeButtonWidth.toFloat()
            state = w.isEnabled
        }
        offsetY += 14
        espToggle = toggleSetting {
            title = "gavinsmod.settings.esp"
            topLeft = PointF(paddingX.toFloat(), offsetY.toFloat())
            width = (wholeButtonWidth / 2 - padding / 2).toFloat()
            state = w.renderEsp
        }
        tracerToggle = toggleSetting {
            title = "gavinsmod.settings.tracer"
            topLeft = PointF((offsetX + padding + padding / 2 + wholeButtonWidth / 2).toFloat(), (offsetY).toFloat())
            width = (wholeButtonWidth / 2 - padding / 2).toFloat()
            state = w.renderTracer
        }
        offsetY += 28
        overworldToggle = toggleSetting {
            title = "gavinsmod.settings.overworld"
            topLeft = PointF(paddingX.toFloat(), (offsetY).toFloat())
            width = wholeButtonWidth.toFloat()
            state = w.hasDimension(Dimension.OVERWORLD)
        }
        offsetY += 14
        netherToggle = toggleSetting {
            title = "gavinsmod.settings.nether"
            topLeft = PointF(paddingX.toFloat(), (offsetY).toFloat())
            width = wholeButtonWidth.toFloat()
            state = w.hasDimension(Dimension.NETHER)
        }
        offsetY += 14
        endToggle = toggleSetting {
            title = "gavinsmod.settings.end"
            topLeft = PointF(paddingX.toFloat(), (offsetY).toFloat())
            width = wholeButtonWidth.toFloat()
            state = w.hasDimension(Dimension.END)
        }
        offsetY += 14
        xCoordinate = TextFieldWidget(minecraftClient.textRenderer, (paddingX + 11).toInt(),
            offsetY.toInt(), 30, 10, Text.literal(""))
        yCoordinate = TextFieldWidget(minecraftClient.textRenderer, (paddingX + 56).toInt(),
            offsetY.toInt(), 30, 10, Text.literal(""))
        zCoordinate = TextFieldWidget(minecraftClient.textRenderer, (paddingX + 104).toInt(),
            offsetY.toInt(), 30, 10, Text.literal(""))
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
        saveSettings = clickSetting {
            title = "gavinsmod.settings.save"
            topLeft = PointF(paddingX.toFloat(), offsetY.toFloat())
            width = buttonWidth.toFloat()
            color = Colors.GREEN
            callback = { saveCallback() }
        }
        cancelSettings = clickSetting {
            title = "gavinsmod.settings.cancel"
            topLeft = PointF((paddingX + padding + buttonWidth).toFloat(), offsetY.toFloat())
            width = buttonWidth.toFloat()
            color = Colors.YELLOW
            callback = { client!!.setScreen(parent) }
        }
        deleteSettings = clickSetting {
            title = "gavinsmod.settings.delete"
            topLeft = PointF((paddingX + padding * 2 + buttonWidth * 2).toFloat(), offsetY.toFloat())
            width = buttonWidth.toFloat()
            color = Colors.RED
            callback = { deleteCallback() }
        }
        box.canHover = false

        guis.add(saveSettings.gui)
        guis.add(cancelSettings.gui)
        guis.add(deleteSettings.gui)
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
        offsetY = minecraftClient.window.scaledHeight / 2.0f - height / 2.0f
        box.render(drawContext, client!!.textRenderer, mouseX, mouseY, delta)
        super.render(drawContext, mouseX, mouseY, delta)
        guis.forEach(Consumer { obj: Gui -> obj.show() })
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Name: "),
            paddingX.toInt(),
            (textField.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("X:"),
            (paddingX + 1).toInt(),
            (xCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Y:"),
            (paddingX + 46).toInt(),
            (yCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Z:"),
            (paddingX + 94).toInt(),
            (zCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Dimensions"),
            (paddingX + 1).toInt(),
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