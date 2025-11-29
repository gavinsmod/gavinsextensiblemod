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

import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.Direction
import com.peasenet.gavui.util.GuiUtil
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import java.util.function.Consumer
import kotlin.math.ceil
import kotlin.math.min

/**
 *
 * @author GT3CH1
 * @version 11-29-2025
 * @since 02-01-2025
 */
open class GuiScroll(builder: GuiBuilder<out GuiScroll>) : GuiDropdown(builder) {

    private var defaultMaxChildren: Int = 0
    private var maxChildren = 0
    private var page = 0
    private var numPages = 0

    init {
        for (child in builder.children) {
            child.width = width
            children.add(child)
            if (direction == Direction.RIGHT)
                child.position = PointF(x2 + 14, y2 + children.size * 14)
        }
        direction = builder.direction
        defaultMaxChildren = builder.defaultMaxChildren
        maxChildren = builder.maxChildren
        maxChildren = children.size.coerceAtMost(maxChildren)
        numPages = ceil(children.size.toDouble() / maxChildren).toInt()
        page = 0
    }

    override fun render(
        drawContext: DrawContext,
        tr: TextRenderer,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        if (isHidden)
            return
        var bg = (if (isParent) GavUI.parentColor() else GavUI.backgroundColor()).withAlpha(GavUI.alpha)
        val childHasMouse = children.any { it.mouseWithinGui(mouseX, mouseY) }
        if (mouseWithinGui(mouseX, mouseY) && !childHasMouse) {
            bg = bg.brighten(0.5f)
        }
        GuiUtil.fill(box, drawContext, bg)
        drawContext.state.goUpLayer()
        GuiUtil.drawOutline(box, drawContext, GavUI.borderColor().withAlpha(GavUI.alpha))
        drawContext.state.goUpLayer()
        var textColor = if (frozen) GavUI.frozenColor() else GavUI.textColor()
        if (title != null) {
            if (textColor.similarity(bg) < 0.2f) {
                textColor = textColor.invert()
                if (textColor.similarity(bg) < 0.2f) textColor = Colors.WHITE
            }
            val center = (x + (width / 2) - (tr.getWidth(title)) / 2).toInt()
            drawText(drawContext, tr, title!!, center, (y + 2).toInt(), textColor)
        }
        updateSymbol()
        drawSymbol(drawContext, tr, textColor, -2f, 0f)
        if (drawBorder) GuiUtil.drawOutline(GavUI.borderColor(), box, drawContext.matrices)
        if (!isOpen) return
        resetChildPos()
        if (page < 0) page = 0
        if (page >= numPages) page = numPages - 1
        renderChildren(drawContext, tr, mouseX, mouseY, delta)

    }

    private fun renderChildren(
        drawContext: DrawContext,
        tr: TextRenderer,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        val renderableChildren = ArrayList<Gui>()
        for (i in children.indices) {
            val child = children[i]
            if (i < page * maxChildren || i >= (page + 1) * maxChildren) {
                child.hide()
                continue
            } else {
                child.show()
            }
            if (shouldDrawScrollBar()) {
                child.shrinkForScrollbar(this)
                drawScrollBox(drawContext)
                drawScrollBar(drawContext)
            }
            if ((!child.isParent && child !is GuiCycle)) {
                child.backgroundColor = GavUI.backgroundColor()
            }
            renderableChildren.add(child)
        }
        drawContext.state.goUpLayer()
        for (child in renderableChildren) {
            child.render(drawContext, tr, mouseX, mouseY, delta)
        }
    }


    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (hasChildren() && isOpen) {
            var visibleChildren = 0
            for (gui in children) {
                if(!gui.isHidden) visibleChildren++
                if (!gui.isHidden && gui.mouseWithinGui(mouseX.toInt(), mouseY.toInt()) && gui is GuiScroll) {
                    if (gui.isOpen) {
                        val scrolled = gui.mouseScrolled(mouseX, mouseY, amount)
                        if (scrolled) {
                            return true
                        }
                    } else {
                        doScroll(amount)
                        return true
                    }
                }
            }
            // from x to y2 + (14*visibleChildren)
            if (mouseX in x..x2 && mouseY >= y && mouseY <= y2 + (14 * visibleChildren)) {
                doScroll(amount)
                return true
            }
        }
        if (mouseWithinGui(mouseX, mouseY)) {
            doScroll(amount)
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    /**
     * Scrolls the list either up or down based off the given scroll value. A positive value will scroll up,
     * where a negative value will scroll down.
     *
     * @param scroll - The scroll value.
     */
    private fun doScroll(scroll: Double) {
        if (!isOpen) return
        if (scroll > 0) scrollUp()
        else scrollDown()
        resetChildPos()
    }

    /**
     * Resets all the children's positions.
     */
    private fun resetChildPos() {
        var modIndex = 0
        for (i in page * maxChildren until page * maxChildren + maxChildren) {
            if (i >= children.size) break
            val gui = children[i]
            if (gui is GuiDraggable) (gui as GuiScroll).resetChildPos()
            when (direction) {
                Direction.DOWN -> gui.position = PointF(x, y2 + 2 + (modIndex * 14))
                Direction.RIGHT -> gui.position = PointF(x2 + 7, y + (modIndex * 14))
            }
            modIndex++
        }
    }

    /**
     * Scrolls the page "up" by one.
     */
    private fun scrollUp() {
        if (page > 0) page--
    }

    /**
     * Scrolls the page "down" by one.
     */
    private fun scrollDown() {
        if (page < numPages - 1) page++
    }

    /**
     * Whether the scrollbar should be drawn.
     *
     * @return Whether the scrollbar should be drawn.
     */
    fun shouldDrawScrollBar(): Boolean {
        return children.size > this.maxChildren
    }

    /**
     * Draws the box that contains the scrollbar.
     *
     * @param drawContext - The draw context.
     */
    private fun drawScrollBox(drawContext: DrawContext) {
        var scrollBoxX = x2 - 5f
        var scrollBoxY = (y2) + 2f
        val scrollBoxHeight = getScrollBoxHeight()
        if (direction == Direction.RIGHT) {
            scrollBoxX = children[page * maxChildren].x2 + 1
            scrollBoxY = y
        }
        val box = BoxF(PointF(scrollBoxX, scrollBoxY), 5f, scrollBoxHeight)
        GuiUtil.drawOutline(box, drawContext, GavUI.backgroundColor())
//        GuiUtil.drawOutline(GavUI.borderColor(), box, matrixStack)
    }

    /**
     * Draws the scrollbar.
     *
     * @param drawContext - The draw context.
     */
    private fun drawScrollBar(drawContext: DrawContext) {
        val scrollBoxHeight = getScrollBoxHeight()
        var scrollBarY = (scrollBoxHeight * (page / numPages.toFloat())) + y2 + 3
        var scrollBarX = x2 - 4
        var scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)))
        if (direction == Direction.RIGHT) {
            // set scrollbarY to (1/page) * scrollBoxHeight
            scrollBarY = (scrollBoxHeight * (page / numPages.toFloat())) + y + 1
            scrollBarX = children[page * maxChildren].x2 + 2
            scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)))
        }
        val box = BoxF(PointF(scrollBarX, scrollBarY), 3f, scrollBarY2 - scrollBarY - 2f)
        GuiUtil.fill(box, drawContext, Colors.WHITE)
    }

    /**
     * Gets the height of the scrollbox.
     * a    *
     *
     * @return The height of the scrollbox.
     */
    private fun getScrollBoxHeight(): Float {
        return ((maxChildren - 1) * 14 + this.height + 0)
    }

    override fun addElement(child: Gui) {
        child.width = this.width
        child.height = this.height
        children.add(child)
        if (direction == Direction.RIGHT) child.position = PointF(x2 + 14, y2 + (children.size) * 12)
        maxChildren = min(children.size.toDouble(), defaultMaxChildren.toDouble()).toInt()
        numPages = ceil(children.size.toDouble() / maxChildren.toDouble()).toInt()
        if (shouldDrawScrollBar())
            children.forEach(Consumer { c: Gui -> c.shrinkForScrollbar(this) })
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isHidden) return false
        if (mouseWithinGui(mouseX, mouseY)) {
            clickedGui = this
            if (clickedOnChild(mouseX, mouseY, button))
                return true
            if (button == 1 && isParent) {
                frozen = !frozen
                return true
            }
            toggleMenu()
            return true
        }
        return clickedOnScrollBar(mouseX, mouseY)
    }

    fun clickedOnScrollBar(x: Double, y: Double): Boolean {
        for (child in children) if (child is GuiScroll && child.clickedOnScrollBar(x, y)) return true
        return (shouldDrawScrollBar() && clickedOnScrollBox(x, y))
    }

    private fun clickedOnScrollBox(x: Double, y: Double): Boolean {
        var scrollBoxX = x2 - 5
        var scrollBoxY = (y2) + 2
        val scrollBoxHeight = getScrollBoxHeight()
        if (direction == Direction.RIGHT) {
            scrollBoxX = children[page * maxChildren].x2 + 0f
            scrollBoxY = y.toFloat()
        }
        if (x >= scrollBoxX && x <= scrollBoxX + 7 && y >= scrollBoxY && y <= scrollBoxY + scrollBoxHeight) {
            var scrollBarY = (scrollBoxHeight * (page / numPages.toDouble())) + y2 + 3
            var scrollBarX = children[page * maxChildren].x2 + 1
            var scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)))
            if (direction == Direction.RIGHT) {
                // set scrollbarY to (1/page) * scrollBoxHeight
                scrollBarY = (scrollBoxHeight * (page / numPages.toDouble())) + y + 1
                scrollBarX = children[page * maxChildren].x2 + 1
                scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)))
            }
            if (x >= scrollBarX && x <= scrollBarX + 3 && y >= scrollBarY && y <= scrollBarY2) {
                // clicked on the scrollbar
                return true
            }
            if (y < scrollBarY - 5) {
                // clicked above the scrollbar
                scrollUp()
                resetChildPos()
                return true
            }
            if (y > scrollBarY2 - 5) {
                // clicked below the scrollbar
                scrollDown()
                resetChildPos()
                return true
            }
        }
        return false
    }

    /**
     * Whether the given mouse coordinates are within the bounds of the child elements. This will return true if
     * a single child is clicked on.
     *
     * @param x      - The mouse x coordinate.
     * @param y      - The mouse y coordinate.
     * @param button - The mouse button.
     * @return True if a child is clicked on, false otherwise.
     */
    private fun clickedOnChild(x: Double, y: Double, button: Int): Boolean {
        for (i in page * maxChildren until page * maxChildren + maxChildren) {
            if (i >= children.size) break
            val gui = children[i]
            if (gui.isHidden) return false
            if (gui.mouseClicked(x, y, button)) {
                for (child in children) {
                    if (child is GuiDropdown && child != gui && child.isOpen) {
                        child.toggleMenu()
                    }
                    if (child is GuiScroll) {
                        child.clickedOnScrollBar(x, y)
                    }
                }
                gui.show()
                return true
            }
        }
        return false
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (!isOpen && !isParent) return false
        if ((isParent && !frozen) && (mouseX in x..x2 && mouseY >= y && mouseY <= y + 12 || dragging) && clickedGui?.uUID == uUID) {
            if (!draggable) return false
            setMidPoint(PointF(mouseX, mouseY))
            isOpen = false
            children.forEach { it.isHidden = true }
            resetDropdownsLocation()
            dragging = true
            return true
        }
        for (child in children) {
            if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
                return true
            if (child.isHidden || (child as? GuiDropdown)?.isOpen == false) continue

        }
        if (frozen || !isParent ) return false

        return false
    }
}