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

package com.peasenet.mods.esp

import com.peasenet.config.EspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.main.Settings
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.util.listeners.BlockEntityRenderListener
import com.peasenet.util.listeners.RenderListener
import org.lwjgl.glfw.GLFW

/**
 * The base class for all esp mods. Extending this class will automatically add the mod to the esp category,
 * as well as a chat command, GUI element, and an optional keybind.
 * For example,
 * ~~~
 * class ModExampleEsp() : EspMod("example_esp", "exampleesp")
 * ~~~
 * This class extends the [Mod] class, so it has all the same methods and properties.
 * By extending this class, you must implement the [BlockEntityRenderListener] interface.
 * @param translationKey The translation key for the mod's name.
 * @param chatCommand The chat command for the mod.
 * @param keyBinding The keybind for the mod. Defaults to [GLFW.GLFW_KEY_UNKNOWN].
 * @see Mod
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 07-18-2023
 */
abstract class EspMod<T>(
    translationKey: String, chatCommand: String, keyBinding: Int = GLFW.GLFW_KEY_UNKNOWN
) : Mod(
    translationKey, chatCommand, ModCategory.ESP, keyBinding
), RenderListener {
    protected var espList: MutableList<T> = ArrayList()

    companion object {
        val config: EspConfig
            get() {
                return Settings.getConfig("esp")
            }
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(RenderListener::class.java, this)
    }

    abstract fun getColor(): Color

    fun getAlpha(): Float = config.alpha
}
