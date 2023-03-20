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
import com.peasenet.settings.ToggleSetting
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3i
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.floor

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A gui that allows the user to add, delete, or modify a waypoint.
 */
class GuiWaypoint : GuiElement {
    /**
     * The text field used to name the waypoint.
     */
    var textField: TextFieldWidget? = null

    /**
     * The x coordinate text field.
     */
    var xCoordinate: TextFieldWidget? = null

    /**
     * The y coordinate text field.
     */
    var yCoordinate: TextFieldWidget? = null

    /**
     * The z coordinate text field.
     */
    var zCoordinate: TextFieldWidget? = null

    /**
     * The button used to save the waypoint.
     */
    var saveSettings = ClickSetting("gavinsmod.settings.save")

    /**
     * The button used to cancel the waypoint.
     */
    var cancelSettings = ClickSetting("gavinsmod.settings.cancel")

    /**
     * The button used to delete the waypoint.
     */
    var deleteSettings = ClickSetting("gavinsmod.settings.delete")

    /**
     * The button used to toggle the waypoint's visibility.
     */
    var waypointToggle = ToggleSetting("gavinsmod.settings.enabled")

    /**
     * The button used to toggle the waypoint's esp.
     */
    var espToggle = ToggleSetting("gavinsmod.settings.esp")

    /**
     * The button used to toggle the waypoint's tracer.
     */
    var tracerToggle = ToggleSetting("gavinsmod.settings.tracer")

    /**
     * The background box.
     */
    var box: Gui? = null

    /**
     * The button that is used to change the waypoints color.
     */
    var colorCycle = ColorSetting("gavinsmod.settings.render.waypoints.color", Colors.BLUE)

    /**
     * The width of the gui.
     */
    var width = 145

    /**
     * The height of the gui.
     */
    var height = 110

    /**
     * The padding of each element.
     */
    var padding = 5

    /**
     * The offset and padding in the x and y planes.
     */
    var offsetX = 0
    var paddingX = 0
    var offsetY = 0
    var paddingY = 0

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
        var dimension = player!!.getWorld().dimension.effects.path
        saveSettings.setCallback {
            val flooredPos = flooredPlayerPos
            w = Waypoint(flooredPos)
            w!!.setName(textField!!.text)
            w!!.color = colorCycle.color
            w!!.isEnabled = waypointToggle.value
            w!!.isEspEnabled = espToggle.value
            w!!.isTracerEnabled = tracerToggle.value
            w!!.x = xCoordinate!!.text.toInt()
            w!!.y = yCoordinate!!.text.toInt()
            w!!.z = zCoordinate!!.text.toInt()
            w!!.dimension = dimension
            GavinsMod.waypointConfig!!.addWaypoint(w!!)
            getMod("waypoints").reloadSettings()
            GavinsMod.guiSettings!!.reloadGui()
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
        saveSettings.setCallback {
            GavinsMod.waypointConfig!!.removeWaypoint(w)
            w.setName(textField!!.text)
            w.color = colorCycle.color
            w.isEnabled = waypointToggle.value
            w.isEspEnabled = espToggle.value
            w.isTracerEnabled = tracerToggle.value
            w.x = xCoordinate!!.text.toInt()
            w.y = yCoordinate!!.text.toInt()
            w.z = zCoordinate!!.text.toInt()
            GavinsMod.waypointConfig!!.addWaypoint(w)
            getMod("waypoints").reloadSettings()
            GavinsMod.guiSettings!!.reloadGui()
            parent = GavinsMod.guiSettings
            close()
        }
        cancelSettings.setCallback { client!!.setScreen(parent) }
        deleteSettings.setCallback {
            GavinsMod.waypointConfig!!.removeWaypoint(w)
            getMod("waypoints").reloadSettings()
            GavinsMod.guiSettings!!.reloadGui()
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
    }

    private val flooredPlayerPos: Vec3i
        get() {
            val playerPos = player!!.getPos()
            return Vec3i(
                floor(playerPos.x).toInt(),
                floor(playerPos.y).toInt() + 1,
                floor(playerPos.z).toInt()
            )
        }

    override fun init() {
        parent = GavinsMod.guiSettings
        offsetX = minecraftClient.window.scaledWidth / 2 - width / 2
        offsetY = minecraftClient.window.scaledHeight / 2 - height / 2
        paddingX = offsetX + padding
        paddingY = offsetY + padding
        box = Gui(PointF(offsetX.toFloat(), offsetY.toFloat()), width, height, Text.literal(""))
        textField = TextFieldWidget(minecraftClient.textRenderer, offsetX + 40, offsetY + 10, 100, 10, Text.literal(""))
        xCoordinate = TextFieldWidget(
            minecraftClient.textRenderer,
            paddingX + 11,
            offsetY + 77 + padding,
            30,
            10,
            Text.literal("")
        )
        yCoordinate = TextFieldWidget(
            minecraftClient.textRenderer,
            paddingX + 56,
            offsetY + 77 + padding,
            30,
            10,
            Text.literal("")
        )
        zCoordinate = TextFieldWidget(
            minecraftClient.textRenderer,
            paddingX + 101,
            offsetY + 77 + padding,
            30,
            10,
            Text.literal("")
        )
        // make sure xCoordinate only contains numbers
        if (w != null) {
            textField!!.text = w!!.name
            xCoordinate!!.text = w!!.x.toString()
            yCoordinate!!.text = w!!.y.toString()
            zCoordinate!!.text = w!!.z.toString()
        } else {
            val playerPos = flooredPlayerPos
            xCoordinate!!.text = playerPos.x.toString()
            yCoordinate!!.text = playerPos.y.toString()
            zCoordinate!!.text = playerPos.z.toString()
        }
        xCoordinate!!.setTextPredicate(checkIfSignedInt())
        yCoordinate!!.setTextPredicate(checkIfSignedInt())
        zCoordinate!!.setTextPredicate(checkIfSignedInt())
        addSelectableChild(textField)
        addSelectableChild(xCoordinate)
        addSelectableChild(yCoordinate)
        addSelectableChild(zCoordinate)
        focused = textField
        val buttonWidth = 42
        val wholeButtonWidth = buttonWidth * 3 + padding * 2
        val threeButtonY = offsetY + 34 + padding
        colorCycle.setWidth(wholeButtonWidth)
        colorCycle.gui.position = PointF(paddingX.toFloat(), (offsetY + 20 + padding).toFloat())
        saveSettings.gui.position = PointF(paddingX.toFloat(), threeButtonY.toFloat())
        saveSettings.gui.width = buttonWidth.toFloat()
        saveSettings.gui.setBackground(Colors.GREEN)
        cancelSettings.gui.position = PointF((paddingX + padding + buttonWidth).toFloat(), threeButtonY.toFloat())
        cancelSettings.gui.width = buttonWidth.toFloat()
        cancelSettings.gui.setBackground(Colors.YELLOW)
        deleteSettings.gui.position =
            PointF((paddingX + padding * 2 + buttonWidth * 2).toFloat(), threeButtonY.toFloat())
        deleteSettings.gui.width = buttonWidth.toFloat()
        deleteSettings.gui.setBackground(Colors.RED)
        waypointToggle.gui.position = PointF(paddingX.toFloat(), (offsetY + padding + 48).toFloat())
        waypointToggle.gui.width = wholeButtonWidth.toFloat()
        espToggle.gui.position = PointF(paddingX.toFloat(), (offsetY + 62 + padding).toFloat())
        espToggle.gui.width = (wholeButtonWidth / 2 - padding / 2).toFloat()
        tracerToggle.gui.position = PointF(
            (offsetX + padding + padding / 2 + wholeButtonWidth / 2).toFloat(),
            (offsetY + 62 + padding).toFloat()
        )
        tracerToggle.gui.width = (wholeButtonWidth / 2 - padding / 2).toFloat()
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
                    val i = s.toInt()
                    return@Predicate i >= Int.MIN_VALUE && i <= Int.MAX_VALUE
                } catch (e: Exception) {
                    return@Predicate false
                }
            }
            false
        }
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        box!!.render(matrixStack, client!!.textRenderer, mouseX, mouseY, delta)
        client!!.textRenderer.draw(
            matrixStack,
            Text.literal("Name: "),
            paddingX.toFloat(),
            (offsetY + 11).toFloat(),
            GavUISettings.getColor("gui.color.foreground").asInt
        )
        client!!.textRenderer.draw(
            matrixStack,
            Text.literal("X:"),
            (paddingX + 1).toFloat(),
            (offsetY + 78 + padding).toFloat(),
            GavUISettings.getColor("gui.color.foreground").asInt
        )
        client!!.textRenderer.draw(
            matrixStack,
            Text.literal("Y:"),
            (paddingX + 46).toFloat(),
            (offsetY + 78 + padding).toFloat(),
            GavUISettings.getColor("gui.color.foreground").asInt
        )
        client!!.textRenderer.draw(
            matrixStack,
            Text.literal("Z:"),
            (paddingX + 91).toFloat(),
            (offsetY + 78 + padding).toFloat(),
            GavUISettings.getColor("gui.color.foreground").asInt
        )
        var dimension = player!!.getWorld().dimension.effects.path
        if (w?.dimension != null)
            dimension = w!!.dimension
        client!!.textRenderer.draw(
            matrixStack,
            Text.literal("Dimension: $dimension"),
            (paddingX + 1).toFloat(),
            (offsetY + 92 + padding).toFloat(),
            GavUISettings.getColor("gui.color.foreground").asInt
        )


        textField!!.render(matrixStack, mouseX, mouseY, delta)
        xCoordinate!!.render(matrixStack, mouseX, mouseY, delta)
        yCoordinate!!.render(matrixStack, mouseX, mouseY, delta)
        zCoordinate!!.render(matrixStack, mouseX, mouseY, delta)
        guis.forEach(Consumer { obj: Gui -> obj.show() })
        super.render(matrixStack, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for (child in guis) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                textField!!.isFocused = false
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }
}