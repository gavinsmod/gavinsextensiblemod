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

package com.peasenet.gui.mod

import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.GuiToggle
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.ToggleSetting
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.resource.language.I18n
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.Registries
import net.minecraft.text.Text

/**
 *
 * The base class for all gui mob selection elements, such as the tracer and esp gui.
 *
 * @author gt3ch1
 * @version 04-11-2023
 */
abstract class GuiMobSelection(label: Text) : GuiElement(label) {

    /**
     * The background gui element.
     */
    protected lateinit var box: Gui

    /**
     * The button that moves to a page behind the current one.
     */
    private lateinit var prevButton: GuiClick

    /**
     * The button that moves to a page ahead of the current one.
     */
    private lateinit var nextButton: GuiClick

    /**
     * The x coordinate of the main gui.
     */
    protected var x = 0

    /**
     * The y coordinate of the main gui.
     */
    protected var y = 0

    /**
     * The search field.
     */
    private lateinit var search: TextFieldWidget

    /**
     * The toggle element to show all blocks or just enabled blocks.
     */
    protected lateinit var enabledOnly: GuiToggle


    /**
     * The hostile color setting.
     */
    protected lateinit var hostileColor: ColorSetting

    /**
     * The peaceful color setting.
     */
    protected lateinit var peacefulColor: ColorSetting

    /**
     * The toggle element to show only peaceful mobs.
     */
    protected lateinit var peacefulToggle: ToggleSetting

    /**
     * The toggle element to show only hostile mobs.
     */
    protected lateinit var hostileToggle: ToggleSetting

    protected companion object {
        const val blocksPerRow: Int = 10
        const val blocksPerColumn = 8
        const val blockOffset: Int = 18
        const val searchMaxWidth = 150
        const val pageWidth = blocksPerRow * blockOffset
        const val pageHeight = blocksPerColumn * blockOffset
        const val itemsPerPage = blocksPerRow * blocksPerColumn
        var items = ArrayList<ItemStack>()
        var visibleItems = ArrayList<ItemStack>()
        val numPages: Int
            get() = visibleItems.size / (blocksPerRow * blocksPerColumn)
        var currentPage = 0
        val pageOffset: Int
            get() = currentPage * itemsPerPage
    }

    /**
     * Pages the gui up.
     */
    private fun pageUp() {
        if (currentPage < numPages) currentPage++
    }

    /**
     * Pages the gui down.
     */
    private fun pageDown() {
        if (currentPage > 0) currentPage--
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        search.isFocused = false
        if (guis.any { it.mouseClicked(mouseX, mouseY, button) }) {
            return true
        }
        if (search.isMouseOver(mouseX, mouseY)) {
            search.mouseClicked(mouseX, mouseY, button)
            search.isFocused = true
            return true
        }
        if (!(mouseX > x && mouseX < x + pageWidth && mouseY > y && mouseY < y + pageHeight)) return false
        val blockIndex = ((mouseY - y) / 19).toInt() * blocksPerRow + ((mouseX - x) / 18).toInt()
        if (blockIndex > visibleItems.size - 1) return false
        val block = visibleItems[blockIndex]
        if (button != 0) return false
        handleItemToggle(block)
        return true
    }


    override fun init() {
        val screenWidth = GavinsModClient.minecraftClient.window.scaledWidth
        val screenHeight = GavinsModClient.minecraftClient.window.scaledHeight
        x = screenWidth - screenWidth / 2 - pageWidth / 2
        y = screenHeight - screenHeight / 2 - pageHeight / 2
//        box = Gui(PointF(x.toFloat(), y.toFloat()), pageWidth, pageHeight, Text.literal(""))
//        box.setBackground(Colors.INDIGO)
        box = GuiBuilder()
            .setTopLeft(PointF(x.toFloat(), y.toFloat()))
            .setWidth(pageWidth.toFloat())
            .setHeight(pageHeight.toFloat())
            .setBackgroundColor(Colors.INDIGO)
            .setHoverable(false)
            .build()
        parent = GavinsMod.guiSettings
        items = ArrayList()
        Registries.ENTITY_TYPE.forEach {
            val spawnEgg = SpawnEggItem.forEntity(it)
            if (spawnEgg != null) items.add(spawnEgg.defaultStack)
        }
        visibleItems = items
//        nextButton = GuiClick(PointF((x + pageWidth - 30f), (y + pageHeight).toFloat()), 30, 10, Text.literal("Next"))
        nextButton = GuiBuilder()
            .setTopLeft(PointF((x + pageWidth - 30f), (y + pageHeight).toFloat()))
            .setWidth(30f)
            .setHeight(10f)
            .setTitle(Text.literal("Next"))
            .setCallback { pageUp() }
            .buildClick()
        nextButton.setCallback { pageUp() }
//        prevButton = GuiClick(PointF(x.toFloat(), (y + pageHeight).toFloat()), 30, 10, Text.literal("Prev"))
        prevButton = GuiBuilder()
            .setTopLeft(PointF(x.toFloat(), (y + pageHeight).toFloat()))
            .setWidth(30f)
            .setHeight(10f)
            .setTitle(Text.literal("Prev"))
            .setCallback { pageDown() }
            .buildClick()
        prevButton.setCallback { pageDown() }
        val searchWidth = searchMaxWidth.coerceAtMost(pageWidth)
        search = object :
            TextFieldWidget(textRenderer, (x + (pageWidth - searchWidth) / 2), y - 15, searchWidth, 12, Text.empty()) {
            override fun charTyped(chr: Char, keyCode: Int): Boolean {
                val pressed = super.charTyped(chr, keyCode)
                currentPage = 0
                updateItemList()
                return pressed
            }

            override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
                val pressed = super.keyPressed(keyCode, scanCode, modifiers)
                currentPage = 0
                updateItemList()
                return pressed
            }
        }

        addSelectableChild(search)

        var tw = textRenderer.getWidth("Enabled Only")
        tw = tw.coerceAtLeast(textRenderer.getWidth(hostileColor.getTitle()))
        tw = tw.coerceAtLeast(textRenderer.getWidth(peacefulColor.getTitle()))
        tw = tw.coerceAtLeast(textRenderer.getWidth(hostileToggle.getTitle()))
        tw = tw.coerceAtLeast(textRenderer.getWidth(peacefulToggle.getTitle()))
        tw += 12
        hostileColor.gui.width = (tw).toFloat()
        peacefulColor.gui.width = (tw).toFloat()
        hostileToggle.gui.width = (tw).toFloat()
        peacefulToggle.gui.width = (tw).toFloat()
        enabledOnly.width = (tw).toFloat()

        guis.clear()
        guis.add(box)
        guis.add(enabledOnly)
        guis.add(hostileToggle.gui)
        guis.add(peacefulColor.gui)
        guis.add(peacefulToggle.gui)
        guis.add(hostileColor.gui)
        guis.add(nextButton)
        guis.add(prevButton)
        updateItemList()
        super.init()
    }


    override fun charTyped(chr: Char, keyCode: Int): Boolean {
        search.charTyped(chr, keyCode)
        return super.charTyped(chr, keyCode)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        search.keyPressed(keyCode, scanCode, modifiers)
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    /**
     * Whether the given item is enabled.
     */
    protected abstract fun isItemEnabled(item: ItemStack): Boolean

    /**
     * Handles the toggling of an item when it is clicked from the GUI.
     */
    abstract fun handleItemToggle(item: ItemStack)

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val matrixStack = drawContext.matrices
//        guis.forEach { it.render(drawContext, textRenderer, mouseX, mouseY, delta) }
        search.render(drawContext, mouseX, mouseY, delta)
//        RenderSystem.disableBlend()
        for (i in 0 until itemsPerPage) {
            if ((pageOffset + i) > visibleItems.size - 1) break
            val block = visibleItems[pageOffset + i]
            val blockX = i % blocksPerRow * blockOffset + x + 1
            val blockY = i / blocksPerRow * blockOffset + y + 1
            val boxF = BoxF(blockX.toFloat(), blockY.toFloat(), 16f, 16f)
            if (isItemEnabled(block)) {
                drawContext.fill(
                    blockX,
                    blockY,
                    blockX + 16,
                    blockY + 16,
                    GavUISettings.getColor("gui.color.enabled").getAsInt(0.5f)
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack, 1f)
            }
            if (mouseX > blockX && mouseX < blockX + 16 && mouseY > blockY && mouseY < blockY + 16) {
                drawContext.fill(
                    blockX,
                    blockY,
                    boxF.x2.toInt(),
                    boxF.y1.toInt(),
                    GavUISettings.getColor("gui.color.foreground").brighten(0.5f).getAsInt(0.5f)
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack, 1f)
                drawContext.drawTooltip(textRenderer, Text.translatable(block.translationKey), mouseX, mouseY)
            }
            drawContext.drawItem(block, blockX, blockY)
        }
//        RenderSystem.enableBlend()
        super.render(drawContext, mouseX, mouseY, delta)
    }

    /**
     * Updates the list of visible items so that it only contains items that match the search query.
     */
    fun updateItemList() {
        var itms = items.filter {
            I18n.translate(it.translationKey).lowercase().contains(
                search.text.lowercase()
            )
        }
        if (enabledOnly.isOn) {
            itms = ArrayList(itms.filter {
                isItemEnabled(it)
            })
        }
        visibleItems = ArrayList(itms)

        if (visibleItems.size <= itemsPerPage) {
            prevButton.hide()
            nextButton.hide()
        } else {
            prevButton.show()
            nextButton.show()
        }
    }


}