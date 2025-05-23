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

package com.peasenet.mods.gui

import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import org.lwjgl.glfw.GLFW

/**
 * The base class for all gui mods. Extending this class will automatically add the mod to the gui category,
 * as well as a chat command, GUI element, and an optional keybind.
 * For example,
 * ~~~
 * class ModExampleGui() : GuiMod("example_gui", "examplegui")
 * ~~~
 * This class extends the [Mod] class, so it has all the same methods and properties.
 * You really do not need to extend this class unless you are adding a new main GUI page. Any mod 
 * here will not appear in the main GUI page or appear in chat commands.
 * @param translationKey The translation key for the mod's name.
 * @param chatCommand The chat command for the mod.
 * @param keyBinding The keybind for the mod. Defaults to [GLFW.GLFW_KEY_UNKNOWN].
 * @see Mod
 *
 * @author GT3CH1
 * @version 01-15-2025
 */
abstract class GuiMod(
    translationKey: String,
    chatCommand: String,
    keyBinding: Int = GLFW.GLFW_KEY_UNKNOWN
) : Mod(
    translationKey,
    chatCommand,
    ModCategory.GUI,
    keyBinding
)