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
package com.peasenet.gui.mod.waypoint

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.GavinsModClient.Companion.player
import com.peasenet.main.Mods.Companion.getMod
import com.peasenet.mods.render.waypoints.Waypoint
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.Dimension
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3i
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.floor

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A gui that allows the user to add, delete, or modify a waypoint.
 */
class GuiWaypoint : GuiElement {

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
//    private var waypointToggle = ToggleSetting("gavinsmod.settings.enabled")
    private lateinit var waypointToggle: GuiToggle

    /**
     * The button used to toggle the waypoint's esp.
     */
    private var espToggle: GuiToggle = GuiBuilder()
        .setTitle("gavinsmod.settings.esp")
        .buildToggle()

    private var tracerToggle: GuiToggle = GuiBuilder()
        .setTitle("gavinsmod.settings.tracer")
        .buildToggle()

    private var overworldToggle: GuiToggle = GuiBuilder()
        .setTitle("gavinsmod.settings.overworld")
        .buildToggle()

    private var netherToggle: GuiToggle = GuiBuilder()
        .setTitle("gavinsmod.settings.nether")
        .buildToggle()

    private var endToggle: GuiToggle = GuiBuilder()
        .setTitle("gavinsmod.settings.end")
        .buildToggle()

    /**
     * The background box.
     */
    var box: Gui? = null

    /**
     * The button that is used to change the waypoints color.
     */
//    private var colorCycle = ColorSetting("gavinsmod.settings.render.waypoints.color", Colors.BLUE)
    private lateinit var colorCycle: ColorSetting

    /**
     * The width of the gui.
     */
    var width = 145

    /**
     * The height of the gui.
     */
    var height = 14 * 11

    /**
     * The padding of each element.
     */
    private var padding = 5

    /**
     * The offset and padding in the x and y planes.
     */
    private var offsetX = 0
    private var paddingX = 0
    private var offsetY = 0
    private var paddingY = 0

    /**
     * The waypoint that is being edited.
     */
    private var w: Waypoint?


    /**
     * Creates a gui that allows the user to add a new waypoint.
     */
    constructor() : super(Text.translatable("gavinsmod.mod.render.waypoints")) {
        parent = GavinsMod.guiSettings
        w = null
        setup()
//        cancelSettings.setCallback { client!!.setScreen(parent) }
        guis.add(colorCycle.gui)
        val rand = Colors.getRandomColor()
        colorCycle.setColorIndex(rand)
        colorCycle.color = Colors.COLORS[rand]
        waypointToggle.setState(true)
        espToggle.setState(true)
        tracerToggle.setState(true)
        guis.add(saveSettings)
        guis.add(cancelSettings)
        guis.add(deleteSettings)
        guis.add(waypointToggle)
        guis.add(espToggle)
        guis.add(tracerToggle)
        guis.add(overworldToggle)
        guis.add(netherToggle)
        guis.add(endToggle)
        saveSettings.setCallback {
            saveCallback()
        }
    }

    private fun saveCallback() {
        val flooredPos = flooredPlayerPos
        w = Waypoint(flooredPos)
        w!!.setName(textField.text)
        w!!.color = colorCycle.color
        w!!.isEnabled = waypointToggle.isOn
        w!!.isEspEnabled = espToggle.isOn
        w!!.isTracerEnabled = tracerToggle.isOn
        w!!.x = xCoordinate.text.toInt()
        w!!.y = yCoordinate.text.toInt()
        w!!.z = zCoordinate.text.toInt()
        if (overworldToggle.isOn) w!!.addDimension(Dimension.OVERWORLD)
        if (netherToggle.isOn) w!!.addDimension(Dimension.NETHER)
        if (endToggle.isOn) w!!.addDimension(Dimension.END)
        GavinsMod.waypointConfig.addWaypoint(w!!)
        getMod("waypoints").reloadSettings()
        GavinsMod.guiSettings.reloadGui()
        parent = GavinsMod.guiSettings
        close()
    }

    /**
     * Creates a gui that allows the user to edit or delete an existing waypoint.
     *
     * @param w - The waypoint to edit.
     */
    constructor(w: Waypoint) : super(Text.translatable("gavinsmod.mod.render.waypoints")) {
        this.w = w
        setup()
        waypointToggle.setState(w.isEnabled)
        colorCycle.color = w.color!!
        colorCycle.setColorIndex(Colors.getColorIndex(w.color!!))
        overworldToggle.setState(w.hasDimension(Dimension.OVERWORLD))
        netherToggle.setState(w.hasDimension(Dimension.NETHER))
        endToggle.setState(w.hasDimension(Dimension.END))
        saveSettings.setCallback {
            GavinsMod.waypointConfig.removeWaypoint(w)
            w.setName(textField.text)
            w.color = colorCycle.color
            w.isEnabled = waypointToggle.isOn
            w.isEspEnabled = espToggle.isOn
            w.isTracerEnabled = tracerToggle.isOn
            w.x = xCoordinate.text.toInt()
            w.y = yCoordinate.text.toInt()
            w.z = zCoordinate.text.toInt()
            w.clearDimensions()
            if (overworldToggle.isOn) w.addDimension(Dimension.OVERWORLD)
            if (netherToggle.isOn) w.addDimension(Dimension.NETHER)
            if (endToggle.isOn) w.addDimension(Dimension.END)
            GavinsMod.waypointConfig.addWaypoint(w)
            getMod("waypoints").reloadSettings()
            GavinsMod.guiSettings.reloadGui()
            parent = GavinsMod.guiSettings
            close()
        }
        cancelSettings.setCallback { client!!.setScreen(parent) }
        deleteSettings.setCallback {
            GavinsMod.waypointConfig.removeWaypoint(w)
            getMod("waypoints").reloadSettings()
            GavinsMod.guiSettings.reloadGui()
            close()
        }
        espToggle.setState(w.isEspEnabled)
        tracerToggle.setState(w.isTracerEnabled)
        guis.add(colorCycle.gui)
        guis.add(saveSettings)
        guis.add(cancelSettings)
        guis.add(deleteSettings)
        guis.add(waypointToggle)
        guis.add(espToggle)
        guis.add(tracerToggle)
        guis.add(overworldToggle)
        guis.add(netherToggle)
        guis.add(endToggle)
    }

    private val flooredPlayerPos: Vec3i
        get() {
            val playerPos = player!!.getPos()
            return Vec3i(floor(playerPos.x).toInt(), floor(playerPos.y).toInt() + 1, floor(playerPos.z).toInt())
        }

    private fun setup() {
        parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding
        box = GuiBuilder()
            .setTopLeft(PointF(offsetX.toFloat(), offsetY.toFloat()))
            .setWidth(width.toFloat())
            .setHeight(height.toFloat())
            .setTitle(Text.literal(""))
            .build()
        textField = TextFieldWidget(minecraftClient.textRenderer, offsetX + 40, offsetY + 10, 100, 10, Text.literal(""))

        focused = textField
        val buttonWidth = 42
        val wholeButtonWidth = buttonWidth * 3 + padding * 2
        offsetY += 20 + padding

        colorCycle = SettingBuilder()
            .setTitle("gavinsmod.settings.render.waypoints.color")
            .setColor(Colors.BLUE)
            .setTopLeft(PointF(paddingX, offsetY))
            .setWidth((wholeButtonWidth).toFloat())
            .setHoverable(true)
            .buildColorSetting()

        offsetY += 14;
        waypointToggle = GuiBuilder()
            .setTitle("gavinsmod.settings.enabled")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth((wholeButtonWidth).toFloat())
            .setHoverable(true)
            .buildToggle()

        offsetY += 14

        espToggle = GuiBuilder()
            .setTitle("gavinsmod.settings.esp")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth((wholeButtonWidth / 2 - padding / 2).toFloat())
            .setHoverable(true)
            .buildToggle()

        tracerToggle = GuiBuilder()
            .setTitle("gavinsmod.settings.tracer")
            .setTopLeft(PointF((offsetX + padding + padding / 2 + wholeButtonWidth / 2).toFloat(), (offsetY).toFloat()))
            .setWidth((wholeButtonWidth / 2 - padding / 2).toFloat())
            .setHoverable(true)
            .buildToggle()

        offsetY += 28

        overworldToggle = GuiBuilder()
            .setTitle("gavinsmod.settings.overworld")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth(wholeButtonWidth.toFloat())
            .setHoverable(true)
            .buildToggle()


        offsetY += 14
        netherToggle = GuiBuilder()
            .setTitle("gavinsmod.settings.nether")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth(wholeButtonWidth.toFloat())
            .setHoverable(true)
            .buildToggle()

        offsetY += 14

        endToggle = GuiBuilder()
            .setTitle("gavinsmod.settings.end")
            .setTopLeft(PointF(paddingX.toFloat(), (offsetY).toFloat()))
            .setWidth(wholeButtonWidth.toFloat())
            .setHoverable(true)
            .buildToggle()
        offsetY += 14
        xCoordinate = TextFieldWidget(minecraftClient.textRenderer, paddingX + 11, offsetY, 30, 10, Text.literal(""))
        yCoordinate = TextFieldWidget(minecraftClient.textRenderer, paddingX + 56, offsetY, 30, 10, Text.literal(""))
        zCoordinate = TextFieldWidget(minecraftClient.textRenderer, paddingX + 101, offsetY, 30, 10, Text.literal(""))
        // make sure xCoordinate only contains numbers
        if (w != null) {
            textField.text = w!!.name
            xCoordinate.text = w!!.x.toString()
            yCoordinate.text = w!!.y.toString()
            zCoordinate.text = w!!.z.toString()
        } else {
            val playerPos = flooredPlayerPos
            xCoordinate.text = playerPos.x.toString()
            yCoordinate.text = playerPos.y.toString()
            zCoordinate.text = playerPos.z.toString()
        }
        xCoordinate.setTextPredicate(checkIfSignedInt())
        yCoordinate.setTextPredicate(checkIfSignedInt())
        zCoordinate.setTextPredicate(checkIfSignedInt())
        addSelectableChild(textField)
        addSelectableChild(xCoordinate)
        addSelectableChild(yCoordinate)
        addSelectableChild(zCoordinate)
        offsetY += 14
        saveSettings = GuiBuilder()
            .setTitle("gavinsmod.settings.save")
            .setTopLeft(PointF(paddingX.toFloat(), offsetY.toFloat()))
            .setWidth(buttonWidth.toFloat())
            .setBackgroundColor(Colors.GREEN)
            .setHoverable(true)
            .buildClick()

        cancelSettings = GuiBuilder()
            .setTitle("gavinsmod.settings.cancel")
            .setTopLeft(PointF((paddingX + padding + buttonWidth).toFloat(), offsetY.toFloat()))
            .setWidth(buttonWidth.toFloat())
            .setBackgroundColor(Colors.YELLOW)
            .setHoverable(true)
            .buildClick()
        deleteSettings = GuiBuilder()
            .setTitle("gavinsmod.settings.delete")
            .setTopLeft(PointF((paddingX + padding * 2 + buttonWidth * 2).toFloat(), offsetY.toFloat()))
            .setWidth(buttonWidth.toFloat())
            .setBackgroundColor(Colors.RED)
            .setHoverable(true)
            .buildClick()
        box!!.isHoverable = false
    }

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
        box!!.render(drawContext, client!!.textRenderer, mouseX, mouseY, delta)
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
            (paddingX + 91),
            (zCoordinate.y + 1),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Dimensions"),
            (paddingX + 1),
            (espToggle.y + 15).toInt(),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        textField.render(drawContext, mouseX, mouseY, delta)
        xCoordinate.render(drawContext, mouseX, mouseY, delta)
        yCoordinate.render(drawContext, mouseX, mouseY, delta)
        zCoordinate.render(drawContext, mouseX, mouseY, delta)
        super.render(drawContext, mouseX, mouseY, delta)
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
}