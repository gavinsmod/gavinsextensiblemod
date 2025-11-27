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

package com.peasenet.gui.mod

import com.peasenet.gavui.GavUI
import com.peasenet.gavui.Gui
import com.peasenet.gavui.GuiBuilder
import com.peasenet.gavui.GuiClick
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.BoxF
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.GavUISettings
import com.peasenet.gavui.util.GuiUtil
import com.peasenet.gui.GuiElement
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Mods.Companion.getMod
import com.peasenet.main.Settings
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.ToggleSetting
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
import net.minecraft.client.resource.language.I18n
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.Registries
import net.minecraft.text.Text

/**
 *
 * The base class for all gui mob selection elements, such as the tracer and esp gui.
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 04-11-2023
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
    protected lateinit var enabledOnly: ToggleSetting


    /**
     * The hostile color setting.
     */
    protected var hostileColor: ColorSetting? = null

    /**
     * The peaceful color setting.
     */
    protected var peacefulColor: ColorSetting? = null

    /**
     * The toggle element to show only peaceful mobs.
     */
    protected var peacefulToggle: ToggleSetting? = null

    /**
     * The toggle element to show only hostile mobs.
     */
    protected var hostileToggle: ToggleSetting? = null

    /**
     * The additional guis to be displayed.
     */
    protected var additionalGuis: ArrayList<Gui> = ArrayList()

    /**
     * Whether to show the peaceful color setting.
     */
    var showPeacefulColor = true

    /**
     * Whether to show the hostile color setting.
     */
    var showHostileColor = true

    /**
     * Whether to show the peaceful toggle setting.
     */
    var showPeacefulToggle = true

    /**
     * Whether to show the hostile toggle setting.
     */
    var showHostileToggle = true

    /**
     * Whether to show the enabled only toggle setting.
     */
    var showEnabledOnly = true

    var blocksPerRow: Int = 0
    var blocksPerPage: Int = 0
    var blocksPerColumn: Int = 0

    var m_width: Int = 0
    var m_height: Int = 0

    protected companion object {
        const val COLUMNS: Int = 14
        private const val ROWS = 6
        const val BLOCK_OFFSET: Int = 19
        const val SEARCH_MAX_WIDTH = 150
        const val PAGE_WIDTH = COLUMNS * BLOCK_OFFSET
        const val PAGE_HEIGHT = ROWS * BLOCK_OFFSET
        const val ITEMS_PER_PAGE = COLUMNS * ROWS
        var items = ArrayList<ItemStack>()
        var visibleItems = ArrayList<ItemStack>()
        val numPages: Int
            get() = visibleItems.size / (COLUMNS * ROWS)
        var currentPage = 0
        val pageOffset: Int
            get() = currentPage * ITEMS_PER_PAGE
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

    private fun isHovering(mouseX: Int, blockX: Int, mouseY: Int, blockY: Int) =
        mouseX >= blockX && mouseX < blockX + 16 && mouseY >= blockY && mouseY < blockY + 16

    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
        val mouseX = click.x
        val mouseY = click.y
        val button = click.button()
        search.isFocused = false
        if (guis.any { it.mouseClicked(mouseX, mouseY, button) }) {
            return true
        }
        if (search.isMouseOver(mouseX, mouseY)) {
            search.mouseClicked(click, doubled)
            search.isFocused = true
            return true
        }
        // get the cell that the mouse has been clicked on
        for (i in 0 until ITEMS_PER_PAGE) {
            val blockX = i % blocksPerRow * BLOCK_OFFSET + x + 2
            val blockY = i / blocksPerRow * BLOCK_OFFSET + y + 3
            val boxF = BoxF(blockX.toFloat(), blockY.toFloat(), 1f, 1f)
            val isHovering = isHovering(mouseX.toInt(), blockX, mouseY.toInt(), blockY)
            if (isHovering) {
                // clicked on a block
                val block = visibleItems.toTypedArray()[i]
                if (button != 0) return false
                handleItemToggle(block)
                return true;
            }
        }
        return false
    }


    override fun init() {
        val screenWidth = GavinsModClient.minecraftClient.window.scaledWidth
        val screenHeight = GavinsModClient.minecraftClient.window.scaledHeight
        m_width = COLUMNS * BLOCK_OFFSET + 3
        m_height = ROWS * BLOCK_OFFSET + 3
        x = (screenWidth - m_width) / 2
        y = (screenHeight - m_height) / 2 + 10
        blocksPerRow = m_width / BLOCK_OFFSET
        blocksPerPage = blocksPerRow * blocksPerColumn
        box = GuiBuilder<Gui>().setTopLeft(x, y).setWidth(m_width.toFloat()).setHeight(m_height.toFloat())
            .setBackgroundColor(Colors.INDIGO).setTransparency(0.5f).setHoverable(false).build()
        parent = GavinsMod.guiSettings
        items = ArrayList()
        Registries.ENTITY_TYPE.forEach {
            val spawnEgg = SpawnEggItem.forEntity(it)
            if (spawnEgg != null) items.add(spawnEgg.defaultStack)
        }
        visibleItems = items
        nextButton =
            GuiBuilder<Gui>().setTopLeft(PointF((x + PAGE_WIDTH - 30f), (y + PAGE_HEIGHT).toFloat())).setWidth(30f)
                .setHeight(10f).setTitle(Text.literal("Next")).setCallback { pageUp() }.buildClick()
        prevButton =
            GuiBuilder<Gui>().setTopLeft(PointF(x.toFloat(), (y + PAGE_HEIGHT).toFloat())).setWidth(30f).setHeight(10f)
                .setTitle(Text.literal("Prev")).setCallback { pageDown() }.buildClick()
        val searchWidth = SEARCH_MAX_WIDTH.coerceAtMost(PAGE_WIDTH)
        search = object :
            TextFieldWidget(textRenderer, (x + (PAGE_WIDTH - searchWidth) / 2), y - 15, searchWidth, 12, Text.empty()) {
            override fun charTyped(input: CharInput): Boolean {
                val pressed = super.charTyped(input)
                currentPage = 0
                updateItemList()
                return pressed
            }

            override fun keyPressed(input: KeyInput): Boolean {
                val pressed = super.keyPressed(input)
                currentPage = 0
                updateItemList()
                return pressed
            }
        }

        addSelectableChild(search)
        var tw = textRenderer.getWidth("Enabled Only")
        if (showHostileColor) tw = tw.coerceAtLeast(textRenderer.getWidth(hostileColor?.gui?.title ?: Text.of("")))
        if (showPeacefulColor) tw = tw.coerceAtLeast(textRenderer.getWidth(peacefulColor?.gui?.title ?: Text.of("")))
        if (showHostileToggle) tw = tw.coerceAtLeast(textRenderer.getWidth(hostileToggle?.gui?.title ?: Text.of("")))
        if (showPeacefulToggle) tw = tw.coerceAtLeast(textRenderer.getWidth(peacefulToggle?.gui?.title ?: Text.of("")))
        if (additionalGuis.isNotEmpty()) {
            for (gui in additionalGuis) {
                tw = tw.coerceAtLeast(textRenderer.getWidth(gui.title ?: Text.of("")))
            }
        }
        tw += 12

        if (showHostileColor) hostileColor?.gui?.width = (tw).toFloat()
        if (showPeacefulColor) peacefulColor?.gui?.width = (tw).toFloat()
        if (showHostileToggle) hostileToggle?.gui?.width = (tw).toFloat()
        if (showPeacefulToggle) peacefulToggle?.gui?.width = (tw).toFloat()
        if (additionalGuis.isNotEmpty()) {
            for (gui in additionalGuis) {
                gui.width = (tw).toFloat()
            }
        }
        enabledOnly.gui.width = (tw).toFloat()

        guis.clear()
        guis.add(box)
        guis.add(enabledOnly.gui)
        if (showHostileToggle) guis.add(hostileToggle!!.gui)
        if (showPeacefulColor) guis.add(peacefulColor!!.gui)
        if (showPeacefulToggle) guis.add(peacefulToggle!!.gui)
        if (showHostileColor) guis.add(hostileColor!!.gui)
        guis.addAll(additionalGuis)
        updateItemList()
        super.init()
    }


    override fun keyPressed(input: KeyInput): Boolean {
        search.keyPressed(input)
        return super.keyPressed(input)
    }

    override fun charTyped(input: CharInput): Boolean {
        search.charTyped(input)
        return super.charTyped(input)
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
        super.render(drawContext, mouseX, mouseY, delta)
        search.render(drawContext, mouseX, mouseY, delta)
        for (i in 0 until ITEMS_PER_PAGE) {
            if ((pageOffset + i) > visibleItems.size - 1) break
            val block = visibleItems[pageOffset + i]
            val blockX = i % blocksPerRow * BLOCK_OFFSET + x + 3
            val blockY = i / blocksPerRow * BLOCK_OFFSET + y + 3
            val boxF = BoxF(PointF(blockX.toFloat(), blockY.toFloat()), 16f, 16f)

            var isHovering = isHovering(mouseX, blockX, mouseY, blockY)
            var enabledColor = GavUISettings.getColor("gui.color.enabled")
            var borderColor = GavUI.borderColor()
            if (isItemEnabled(block)) {
                if (isHovering) enabledColor = enabledColor.brighten(0.25f)
                drawContext.fill(
                    blockX, blockY, blockX + 16, blockY + 16, enabledColor.getAsInt(0.5f)
                )
                GuiUtil.drawOutline(boxF.expand(1), drawContext, borderColor)
            }
            if (isHovering) {
                drawContext.fill(
                    blockX,
                    blockY,
                    boxF.x2.toInt(),
                    boxF.y1.toInt(),
                    GavUISettings.getColor("gui.color.foreground").brighten(0.5f).getAsInt()
                )
                GuiUtil.drawOutline(Colors.WHITE, boxF, matrixStack)
                drawContext.drawTooltip(textRenderer, Text.translatable(block.item.translationKey), mouseX, mouseY)
            }
            drawContext.drawItem(block, blockX, blockY)
        }
    }

    /**
     * Updates the list of visible items so that it only contains items that match the search query.
     */
    fun updateItemList() {
        var itms = items.filter {
            I18n.translate(it.item.translationKey).lowercase().contains(
                search.text.lowercase()
            )
        }
        if (enabledOnly.state) {
            itms = ArrayList(itms.filter {
                isItemEnabled(it)
            })
        }
        visibleItems = ArrayList(itms)

        if (visibleItems.size <= ITEMS_PER_PAGE) {
            prevButton.hide()
            nextButton.hide()
        } else {
            prevButton.show()
            nextButton.show()
        }
    }


}