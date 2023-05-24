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
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.GavinsModClient.Companion.player
import com.peasenet.main.Mods.Companion.getMod
import com.peasenet.mods.render.waypoints.Waypoint
import com.peasenet.settings.ClickSetting
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
    private var saveSettings = ClickSetting("gavinsmod.settings.save")

    /**
     * The button used to cancel the waypoint.
     */
    private var cancelSettings = ClickSetting("gavinsmod.settings.cancel")

    /**
     * The button used to delete the waypoint.
     */
    private var deleteSettings = ClickSetting("gavinsmod.settings.delete")

    /**
     * The button used to toggle the waypoint's visibility.
     */
    private var waypointToggle = ToggleSetting("gavinsmod.settings.enabled")

    /**
     * The button used to toggle the waypoint's esp.
     */
    private var espToggle = ToggleSetting("gavinsmod.settings.esp")

    /**
     * The button used to toggle the waypoint's tracer.
     */
    private var tracerToggle = ToggleSetting("gavinsmod.settings.tracer")

    private var overworldToggle = ToggleSetting("gavinsmod.settings.overworld")
    private var netherToggle = ToggleSetting("gavinsmod.settings.nether")
    private var endToggle = ToggleSetting("gavinsmod.settings.end")

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
        this.colorCycle = SettingBuilder()
            .setTitle("gavinsmod.settings.render.waypoints.color")
            .setColor(Colors.BLUE)
            .buildColorSetting()
        parent = GavinsMod.guiSettings
        w = null
        cancelSettings.setCallback { client!!.setScreen(parent) }
        guis.add(colorCycle.gui)
        val rand = Colors.getRandomColor()
        colorCycle.setColorIndex(rand)
        colorCycle.color = Colors.COLORS[rand]
        waypointToggle.value = true
        espToggle.value = true
        tracerToggle.value = true
        guis.add(saveSettings.gui)
        guis.add(cancelSettings.gui)
        guis.add(deleteSettings.gui)
        guis.add(waypointToggle.gui)
        guis.add(espToggle.gui)
        guis.add(tracerToggle.gui)
        guis.add(overworldToggle.gui)
        guis.add(netherToggle.gui)
        guis.add(endToggle.gui)
        saveSettings.setCallback {
            val flooredPos = flooredPlayerPos
            w = Waypoint(flooredPos)
            w!!.setName(textField.text)
            w!!.color = colorCycle.color
            w!!.isEnabled = waypointToggle.value
            w!!.isEspEnabled = espToggle.value
            w!!.isTracerEnabled = tracerToggle.value
            w!!.x = xCoordinate.text.toInt()
            w!!.y = yCoordinate.text.toInt()
            w!!.z = zCoordinate.text.toInt()
            if (overworldToggle.value) w!!.addDimension(Dimension.OVERWORLD)
            if (netherToggle.value) w!!.addDimension(Dimension.NETHER)
            if (endToggle.value) w!!.addDimension(Dimension.END)
            GavinsMod.waypointConfig.addWaypoint(w!!)
            getMod("waypoints").reloadSettings()
            GavinsMod.guiSettings.reloadGui()
            parent = GavinsMod.guiSettings
            close()
        }
    }

    /**
     * Creates a gui that allows the user to edit or delete an existing waypoint.
     *
     * @param w - The waypoint to edit.
     */
    constructor(w: Waypoint) : super(Text.translatable("gavinsmod.mod.render.waypoints")) {
        this.w = w
        waypointToggle.value = w.isEnabled
        colorCycle.color = w.color!!
        colorCycle.setColorIndex(Colors.getColorIndex(w.color!!))
        overworldToggle.value = w.hasDimension(Dimension.OVERWORLD)
        netherToggle.value = w.hasDimension(Dimension.NETHER)
        endToggle.value = w.hasDimension(Dimension.END)
        saveSettings.setCallback {
            GavinsMod.waypointConfig.removeWaypoint(w)
            w.setName(textField.text)
            w.color = colorCycle.color
            w.isEnabled = waypointToggle.value
            w.isEspEnabled = espToggle.value
            w.isTracerEnabled = tracerToggle.value
            w.x = xCoordinate.text.toInt()
            w.y = yCoordinate.text.toInt()
            w.z = zCoordinate.text.toInt()
            w.clearDimensions()
            if (overworldToggle.value) w.addDimension(Dimension.OVERWORLD)
            if (netherToggle.value) w.addDimension(Dimension.NETHER)
            if (endToggle.value) w.addDimension(Dimension.END)
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
        espToggle.value = w.isEspEnabled
        tracerToggle.value = w.isTracerEnabled
        guis.add(colorCycle.gui)
        guis.add(saveSettings.gui)
        guis.add(cancelSettings.gui)
        guis.add(deleteSettings.gui)
        guis.add(waypointToggle.gui)
        guis.add(espToggle.gui)
        guis.add(tracerToggle.gui)
        guis.add(overworldToggle.gui)
        guis.add(netherToggle.gui)
        guis.add(endToggle.gui)
    }

    private val flooredPlayerPos: Vec3i
        get() {
            val playerPos = player!!.getPos()
            return Vec3i(floor(playerPos.x).toInt(), floor(playerPos.y).toInt() + 1, floor(playerPos.z).toInt())
        }

    override fun init() {
        parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding
        box = Gui(PointF(offsetX.toFloat(), offsetY.toFloat()), width, height, Text.literal(""))
        textField = TextFieldWidget(minecraftClient.textRenderer, offsetX + 40, offsetY + 10, 100, 10, Text.literal(""))


        focused = textField
        val buttonWidth = 42
        val wholeButtonWidth = buttonWidth * 3 + padding * 2
        offsetY + 34 + padding

        colorCycle.setWidth(wholeButtonWidth)
        colorCycle.gui.position = PointF(paddingX.toFloat(), (offsetY + 20 + padding).toFloat())
        offsetY += 34 + padding


//        offsetY = offsetY + padding + 48;
        waypointToggle.gui.position = PointF(paddingX.toFloat(), (offsetY).toFloat())
        waypointToggle.gui.width = wholeButtonWidth.toFloat()
        offsetY += 14
        espToggle.gui.position = PointF(paddingX.toFloat(), (offsetY).toFloat())
        espToggle.gui.width = (wholeButtonWidth / 2 - padding / 2).toFloat()
        tracerToggle.gui.position =
            PointF((offsetX + padding + padding / 2 + wholeButtonWidth / 2).toFloat(), (offsetY).toFloat())
        tracerToggle.gui.width = (wholeButtonWidth / 2 - padding / 2).toFloat()
        offsetY += 28

        // render overworld, nether, and end buttons underneath each other
        overworldToggle.gui.position = PointF(paddingX.toFloat(), (offsetY).toFloat())
        overworldToggle.gui.width = wholeButtonWidth.toFloat()
        offsetY += 14

        netherToggle.gui.position = PointF(paddingX.toFloat(), (offsetY).toFloat())
        netherToggle.gui.width = wholeButtonWidth.toFloat()
        offsetY += 14

        endToggle.gui.position = PointF(paddingX.toFloat(), (offsetY).toFloat())
        endToggle.gui.width = wholeButtonWidth.toFloat()
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
        saveSettings.gui.position = PointF(paddingX.toFloat(), offsetY.toFloat())
        saveSettings.gui.width = buttonWidth.toFloat()
        saveSettings.gui.setBackground(Colors.GREEN)
        saveSettings.gui.isHoverable = true
        cancelSettings.gui.position = PointF((paddingX + padding + buttonWidth).toFloat(), offsetY.toFloat())
        cancelSettings.gui.width = buttonWidth.toFloat()
        cancelSettings.gui.setBackground(Colors.YELLOW)
        cancelSettings.gui.isHoverable = true
        deleteSettings.gui.position = PointF((paddingX + padding * 2 + buttonWidth * 2).toFloat(), offsetY.toFloat())
        deleteSettings.gui.width = buttonWidth.toFloat()
        deleteSettings.gui.setBackground(Colors.RED)
        deleteSettings.gui.isHoverable = true
        box!!.isHoverable = false
        super.init()
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
        val matrixStack = drawContext.matrices
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2

        box!!.render(drawContext, client!!.textRenderer, mouseX, mouseY, delta)

        guis.forEach(Consumer { obj: Gui -> obj.show() })
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Name: "),
            paddingX,
            (offsetY + 11),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("X:"),
            (paddingX + 1),
            (offsetY + 14 * 9 - 2),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Y:"),
            (paddingX + 46),
            (offsetY + 14 * 9 - 2),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Z:"),
            (paddingX + 91),
            (offsetY + 14 * 9 - 2),
            GavUISettings.getColor("gui.color.foreground").asInt,
            false
        )
        drawContext.drawText(
            client!!.textRenderer,
            Text.literal("Dimensions"),
            (paddingX + 1),
            (offsetY + 14 * 5 - 2),
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