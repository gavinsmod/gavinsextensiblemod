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

package com.peasenet.settings

import com.peasenet.gavui.GuiDropdown.Direction
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.callbacks.GuiCallback
import net.minecraft.text.Text

/**
 * A builder that creates a setting.
 * @author GT3CH1
 * @version 09-01-2024
 * @since 05/18/2023
 */
class SettingBuilder {

    /**
     * The symbol for this setting. If set to 0, no symbol will be displayed.
     */
    private var symbol: Char = 0.toChar()

    /**
     * The translation key for this setting.
     */
    private var translationKey: String = ""

    /**
     * The background color for this setting.
     */
    private var color: Color? = null

    /**
     * The current state of this setting.
     */
    private var state: Boolean = false

    /**
     * The current float value of this setting.
     */
    private var value: Float = 0f

    /**
     * The title of this setting.
     */
    private lateinit var title: Text

    /**
     * The callback for this setting.
     */
    private var callback: GuiCallback? = null

    /**
     * The width of the GUI for this setting.
     */
    private var width: Float = 0f

    /**
     * The height of the GUI for this setting.
     */
    private var height: Float = 0f

    /**
     * The position of the GUI for this setting.
     */
    private var guiPosition: PointF = PointF(0f, 0f)

    /**
     * The transparency of the GUI for this setting.
     * Setting to -1f will use the transparency setting in GavUI
     */
    private var transparency: Float = -1f

    /**
     * The dropdown direction for this setting. Defaults to RIGHT.
     */
    private var direction: Direction = Direction.RIGHT

    /**
     * Whether this setting is hoverable.
     */
    private var hoverable: Boolean = true

    /**
     * The maximum number of children for this setting.
     */
    private var maxChildren: Int = 4

    /**
     * The default maximum number of children for this setting.
     */
    private var defaultMaxChildren: Int = 4

    /**
     * Sets whether this setting is hoverable, allowing a slight color change when hovered over.
     * @param hoverable True if this setting is hoverable, false otherwise.
     */
    fun setHoverable(hoverable: Boolean): SettingBuilder {
        this.hoverable = hoverable
        return this
    }

    /**
     * Gets whether this setting is hoverable.
     */
    fun isHoverable(): Boolean {
        return this.hoverable
    }

    /**
     * Sets the dropdown direction for this setting.
     *
     * @param direction The direction to set.
     * @see [Direction]
     */
    fun setDirection(direction: Direction): SettingBuilder {
        this.direction = direction
        return this
    }

    /**
     * Gets the dropdown direction for this setting.
     *
     * @return The dropdown direction for this setting.
     * @see [Direction]
     */
    fun getDirection(): Direction {
        return this.direction
    }

    /**
     * Sets the top left corner of the GUI for this setting.
     *
     * @param x The x coordinate of the top left corner.
     * @param y The y coordinate of the top left corner.
     */
    fun setTopLeft(x: Float, y: Float): SettingBuilder {
        this.guiPosition = PointF(x, y)
        return this
    }

    /**
     * Sets the top left corner of the GUI for this setting.
     *
     * @param point The point to set the top left corner to.
     * @see [PointF]
     */
    fun setTopLeft(point: PointF): SettingBuilder {
        this.guiPosition = point
        return this
    }

    /**
     * Gets the top left corner of the setting as a [PointF].
     */
    fun getTopLeft(): PointF {
        return this.guiPosition
    }

    /**
     * Gets the number of maximum children for this setting in one page.
     */
    fun getMaxChildren(): Int {
        return this.maxChildren
    }

    /**
     * Sets the maximum number of children to be rendered in one page for this setting.
     * @param maxChildren The maximum number of children to be rendered in one page.
     */
    fun setMaxChildren(maxChildren: Int): SettingBuilder {
        this.maxChildren = maxChildren
        return this
    }

    /**
     * Gets the initial number of children to be rendered in one page for this setting.
     * @return The initial number of children to be rendered in one page.
     */
    fun getDefaultMaxChildren(): Int {
        return this.defaultMaxChildren
    }

    /**
     * Sets the initial number of children to be rendered in one page for this setting.
     * @param defaultMaxChildren The initial number of children to be rendered in one page.
     */
    fun setDefaultMaxChildren(defaultMaxChildren: Int): SettingBuilder {
        this.defaultMaxChildren = defaultMaxChildren
        return this
    }

    /**
     * Sets the width of GUI for this setting.
     *
     * @param width The width to set.
     */
    fun setWidth(width: Float): SettingBuilder {
        this.width = width
        return this
    }

    /**
     * Sets the width of GUI for this setting.
     *
     * @param width The width to set.
     */
    fun setWidth(width: Int): SettingBuilder {
        this.width = width.toFloat()
        return this
    }


    /**
     * Sets the height of GUI for this setting.
     *
     * @param height The height to set.
     */
    fun setHeight(height: Int): SettingBuilder {
        this.height = height.toFloat()
        return this
    }

    /**
     * Gets the title of this setting.
     * @return The title of this setting.
     */
    fun getTitle(): Text {
        return this.title
    }

    /**
     * Sets the height of GUI for this setting.
     * @param height The height to set.
     */
    fun setHeight(height: Float): SettingBuilder {
        this.height = height
        return this
    }

    /**
     * Gets the height of the GUI for this setting.
     *
     * @return The height of the GUI for this setting.
     */
    fun getHeight(): Float {
        return this.height
    }

    /**
     * Gets the width of the GUI for this setting.
     * @return The width of the GUI for this setting.
     */
    fun getWidth(): Float {
        return this.width
    }

    /**
     * Sets the title of this setting and translates it.
     *
     * @param translationKey The translation key to translate the title to.
     */
    fun setTitle(translationKey: String): SettingBuilder {
        this.translationKey = translationKey
        setTitle(Text.translatable(translationKey))
        return this
    }

    /**
     * Sets the title of this setting as a literal text.
     *
     * @param title The title to set.
     */
    fun setTitle(title: Text): SettingBuilder {
        this.title = title
        return this
    }

    /**
     * Gets the translation key of this setting.
     *
     * @return The translation key of this setting.
     */
    fun getTranslationKey(): String {
        return this.translationKey
    }

    /**
     * Sets the background color of this setting.
     *
     * @param color The color to set.
     * @see [Color]
     */
    fun setColor(color: Color): SettingBuilder {
        this.color = color
        return this
    }

    /**
     * Sets the boolean state of this setting.
     * @param state The boolean state to set.
     */
    fun setState(state: Boolean): SettingBuilder {
        this.state = state
        return this
    }

    /**
     * Sets the float value of this setting, used for sliders.
     * @param value The float value to set.
     */
    fun setValue(value: Float): SettingBuilder {
        this.value = value
        return this
    }

    /**
     * Gets the float value of this setting.
     */
    fun getValue(): Float {
        return this.value
    }

    /**
     * Gets the boolean state of this setting.
     */
    fun getState(): Boolean {
        return this.state
    }

    /**
     * Gets the symbol of this setting.
     */
    fun getSymbol(): Char {
        return this.symbol
    }

    /**
     * Gets the background color of this setting.
     */
    fun getColor(): Color? {
        return this.color
    }

    /**
     * Builds a [ColorSetting] from this builder.
     * @return The built [ColorSetting].
     */
    fun buildColorSetting(): ColorSetting {
        return ColorSetting(this)
    }

    /**
     * Builds a [ToggleSetting] from this builder.
     * @return The built [ToggleSetting].
     */
    fun buildToggleSetting(): ToggleSetting {
        return ToggleSetting(this)
    }

    /**
     * Builds a [SubSetting] from this builder.
     * @return The built [SubSetting].
     */
    fun buildSubSetting(): SubSetting {
        return SubSetting(this)
    }

    /**
     * Builds a [ClickSetting] from this builder.
     * @return The built [ClickSetting].
     */
    fun buildClickSetting(): ClickSetting {
        return ClickSetting(this)
    }

    /**
     * Builds a [SlideSetting] from this builder.
     * @return The built [SlideSetting].
     */
    fun buildSlider(): SlideSetting {
        return SlideSetting(this)
    }

    /**
     * Gets the transparenct value of this setting.
     */
    fun getTransparency(): Float {
        return this.transparency
    }

    /**
     * Sets the transparency of this setting.
     * @param transparency The transparency to set.
     * If the transparency is set to -1, the transparency value will be configured from GavUI config.
     */
    fun setTransparency(transparency: Float): SettingBuilder {
        this.transparency = transparency
        return this
    }

    /**
     * Sets the background color of this setting.
     * @param color The color to set.
     */
    fun setBackgroundColor(color: Color): SettingBuilder {
        this.color = color
        return this
    }

    /**
     * Sets the callback function of this setting.
     * @param function The callback function to set.
     */
    fun setCallback(function: GuiCallback): SettingBuilder {
        this.callback = function
        return this
    }

    /**
     * Sets the GUI symbol of this setting.
     * @param symbol The symbol to set.
     */
    fun setSymbol(symbol: Char): SettingBuilder {
        this.symbol = symbol
        return this
    }

    /**
     * Gets the callback function of this setting.
     * @return The callback function of this setting.
     */
    fun getCallback(): GuiCallback? {
        return callback
    }

}