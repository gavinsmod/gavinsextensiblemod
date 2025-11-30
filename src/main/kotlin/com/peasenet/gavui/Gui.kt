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
package com.peasenet.gavui

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GuiUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

/**
 * The base class for all gui elements.a
 * @param builder The builder for this gui.
 * @param children The children of this gui.
 * @param title The title of this gui.
 * @param translationKey The translation key of this gui.
 * @param symbol The symbol of this gui.
 * @param isParent Whether this gui is a parent.
 * @param box The box of this gui.
 * @param backgroundColor The background color of this gui.
 * @param canHover Whether this gui can be hovered over.
 * @param drawBorder Whether this gui should draw a border.
 * @param height The height of this gui.
 *
 * @author GT3CH1
 * @version 11-27-2025
 * @since 2/28/2023
 */
open class Gui(
    builder: GuiBuilder<*>,
    var children: ArrayList<Gui> = ArrayList(),
    var title: Component? = builder.title,
    var translationKey: String? = builder.translationKey,
    var symbol: String? = builder.symbol,
    var isParent: Boolean = builder.isParent,
    var box: BoxF = BoxF(builder.topLeft, builder.width, builder.height),
    var backgroundColor: Color = builder.backgroundColor,
    var canHover: Boolean = builder.canHover,
    var drawBorder: Boolean = builder.drawBorder,
    var height: Float = builder.height,
) {
    /**
     * The original position of the gui.
     */
    private val defaultPosition: BoxF

    /**
     * A randomly generated UUID for this gui.
     */
    val uUID: UUID = UUID.randomUUID()

    /**
     * The offset for the symbol.
     */
    var symbolOffsetX: Int = -10

    /**
     * The offset for the symbol.
     */
    var symbolOffsetY: Int = 1

    /**
     * How transparent the gui is.
     */
    private var transparency: Float = -1f
        get() {
            if (field == -1f) {
                return GavUI.alpha
            }
            return field
        }
        set(transparency) {
            if (transparency == -1f) {
                return
            }
            field = transparency.coerceIn(0f, 1f)
        }

    /**
     * Whether this gui is being dragged.
     */
    var dragging: Boolean = false
        set(value) {
            field = value
            for (child in children) child.dragging = (value)
        }

    /**
     * Whether this gui is hidden.
     */
    var isHidden: Boolean = false
        set(hidden) {
            field = hidden
            if (this.hasChildren()) {
                children.forEach(Consumer { obj: Gui -> obj.isHidden = hidden })
            }
        }

    /**
     * Whether this gui has been shrunk for a scrollbar.
     */
    private var shrunkForScroll = false
        set(value) {
            field = value
            if (hasChildren()) for (c in children) c.shrunkForScroll = value
        }

    /**
     * Clears all children from this gui.
     */
    fun clearChildren() {
        children.clear()
    }

    /**
     * Adds an child element to this gui.
     *
     * @param child - The child element to add.
     */
    open fun addElement(child: Gui) {
        if (children.isEmpty()) {
            child.position = PointF(x2 + 100, y2 + 1)
            children.add(child)
            return
        }
        // get last gui
        val lastButton = children[children.size - 1]
        val lastY = lastButton.y2
        // set new gui position
        child.position = PointF(x, lastY + 2)
        children.add(child)
    }

    /**
     * Hides this gui.
     */
    fun hide() {
        isHidden = true
    }

    /**
     * Shows this gui.
     */
    fun show() {
        isHidden = false
    }


    val x: Float
        get() = box.x1

    val y: Float
        get() = box.y1

    val x2: Float
        get() = box.x2

    val y2: Float
        get() = box.y2

    var width: Float
        get() = box.width
        set(width) {
            box = BoxF(box.topLeft, width, box.height)
        }

    /**
     * Shrinks the gui to fit a scrollbar.
     *
     * @param parent - The parent gui.
     */
    fun shrinkForScrollbar(parent: Gui) {
        if (shrunkForScroll) return
        width -= 6
        shrunkForScroll = true
    }

    /**
     * Renders the clickable ui
     *
     * @param drawContext The draw context used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     * @param mouseX      The x coordinate of the mouse.
     * @param mouseY      The y coordinate of the mouse.
     * @param delta       The change in time since the last render.
     */
    open fun render(
        drawContext: GuiGraphics,
        tr: Font,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        if (isHidden) return
        var bg = backgroundColor
        if (mouseWithinGui(mouseX, mouseY) && canHover) bg = bg.brighten(0.25f)
        bg = bg.withAlpha(transparency)
        drawContext.guiRenderState.up()
        GuiUtil.fill(box, drawContext, bg)
        drawContext.guiRenderState.up()
        GuiUtil.drawOutline(box, drawContext, GavUI.borderColor().withAlpha(GavUI.alpha))
        var textColor = GavUI.textColor()
        if (title != null) {
            if (textColor.similarity(bg) < 0.3f) {
                textColor = textColor.invert()
                if (textColor.similarity(bg) < 0.3f) textColor = Colors.WHITE
            }
            val titleH = tr.lineHeight
            val offsetY = (box.height - titleH)
            drawText(drawContext, tr, title!!, x.toInt() + 2, y.toInt() + offsetY.toInt(), textColor)
        }
        drawSymbol(drawContext, tr, textColor, 0f, 2f)

        if (this.drawBorder) GuiUtil.drawOutline(GavUI.borderColor(), box, drawContext.pose())
        renderChildren(drawContext, tr, mouseX, mouseY, delta)
    }

    /**
     * Draws the GUI symbol.
     *
     * @param drawContext - The draw context to use.
     * @param tr          - The text renderer to use.
     */
    protected fun drawSymbol(
        drawContext: GuiGraphics,
        tr: Font,
        color: Color,
        offsetX: Float = 0f,
        offsetY: Float = 0f,
    ) {
        if (symbol == null) return
        val symbolWidth = tr.width(symbol.toString())
        drawContext.drawString(
            tr,
            symbol,
            (x2 - symbolWidth + offsetX).toInt(),
            (y + symbolOffsetY + offsetY).toInt(),
            color.asInt,
            false,
        )
    }

    /**
     * Renders the children of this gui.
     *
     * @param drawContext - The draw context to use to draw the children.
     * @param tr          - The text renderer to use to draw the children.
     * @param mouseX      - The x coordinate of the mouse.
     * @param mouseY      - The y coordinate of the mouse.
     * @param delta       - The change in time since the last render.
     */
    private fun renderChildren(
        drawContext: GuiGraphics,
        tr: Font,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        if (!hasChildren()) return
        for (c in children) c.render(drawContext, tr, mouseX, mouseY, delta)
    }

    /**
     * Checks whether the mouse is clicked.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @return Whether the mouse was clicked.
     */
    open fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return false
    }

    /**
     * Checks whether the mouse was dragged.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param button - The button that was dragged.
     * @param deltaX - The change in x coordinate.
     * @param deltaY - The change in y coordinate.
     * @return Whether the mouse was dragged.
     */
    open fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return false
    }

    /**
     * Checks whether the mouse was scrolled
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param amount - The amount of scroll.
     * @return Whether the mouse was scrolled.
     */
    open fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return false
    }

    /**
     * Whether the current window is being dragged.`
     *
     * @return True if the current window is being dragged.
     */
    @Deprecated("Use dragging instead.", ReplaceWith("dragging"))
    fun isDragging(): Boolean {
        return dragging
    }

    /**
     * Resets the position to the default position.
     */
    open fun resetPosition() {
        box = BoxF.copy(defaultPosition)
    }

    var position: PointF
        get() = box.topLeft
        set(position) {
            box.topLeft = position
        }

    /**
     * Sets the middle of the gui element to the given point.
     *
     * @param position - The point to set the middle of the gui element to.
     */
    fun setMidPoint(position: PointF) {
        box.setMiddle(position)
    }

    /**
     * Gets whether the mouse coordinates are within the bounds of the gui.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @return Whether the mouse coordinates are within the bounds of the gui.
     */
    open fun mouseWithinGui(mouseX: Double, mouseY: Double): Boolean {
        return mouseX in x..x2 && mouseY >= y && mouseY <= y2 && !isHidden
    }

    fun mouseWithinGui(mouseX: Int, mouseY: Int): Boolean {
        return mouseWithinGui(mouseX.toDouble(), mouseY.toDouble())
    }

    /**
     * Whether this element has children.
     *
     * @return True if this element has children.
     */
    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

    fun setShrunkForScrollbar(b: Boolean) {
        shrunkForScroll = b
        if (hasChildren()) for (c in children) c.setShrunkForScrollbar(b)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Gui) {
            return other.uUID == uUID
        }
        return false
    }


    /**
     * Sets the default position of the gui.
     *
     * @param newDefaultPosition - The new default position of the gui.
     */
    fun setDefaultPosition(newDefaultPosition: BoxF) {
        defaultPosition.from(newDefaultPosition)
    }

    //NOTE: I wrote this so that I don't have to deal with the API in drawContext and I can just use the textRenderer directly.
    protected fun drawText(
        drawContext: GuiGraphics,
        textRenderer: Font,
        text: Component,
        x: Int,
        y: Int,
        color: Color,
        shadow: Boolean = false,
    ) {
        drawContext.drawString(textRenderer, text, x, y, color.asInt, shadow)
    }

    override fun hashCode(): Int {
        return uUID.hashCode()
    }

    companion object {
        /**
         * The gui that was clicked.
         */
        @JvmStatic
        var clickedGui: Gui? = null
    }

    init {
        this.title = builder.title
        var w = builder.width
        if (title != null && Minecraft.getInstance().font != null) w = max(
            w.toDouble(),
            Minecraft.getInstance().font.width(title).toDouble()
        ).toFloat()
        box = BoxF(builder.topLeft, w, builder.height)
        defaultPosition = BoxF.copy(box)
        dragging = false
        this.translationKey = builder.translationKey
        position = builder.topLeft
        backgroundColor = builder.backgroundColor
        symbol = (builder.symbol)
        isHidden = builder.isHidden
        canHover = builder.canHover
        transparency = builder.transparency
        drawBorder = builder.drawBorder
        isParent = builder.isParent
    }
}
