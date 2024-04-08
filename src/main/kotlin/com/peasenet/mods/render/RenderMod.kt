/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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

package com.peasenet.mods.render

import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import org.lwjgl.glfw.GLFW

/**
 * The base class for all render mods. Extending this class will automatically add the mod to the render category,
 * as well as a chat command, GUI element, and an optional keybind.
 * For example,
 * ~~~
 * class ModExampleRender() : RenderMod("Example Render", "example_render", "examplerender")
 * ~~~
 * This class extends the [Mod] class, so it has all of the same methods and properties.
 *
 * @param name The name of the mod.
 * @param translationKey The translation key for the mod's name.
 * @param chatCommand The chat command for the mod.
 * @param keyBinding The keybind for the mod. Defaults to [GLFW.GLFW_KEY_UNKNOWN].
 * @see Mod
 *
 * @author GT3CH1
 * @version 07-18-2023
 */
abstract class RenderMod(
    name: String, translationKey: String, chatCommand: String,
    modCategory: ModCategory = ModCategory.RENDER, keyBinding: Int = GLFW.GLFW_KEY_UNKNOWN
) : Mod(
    name,
    translationKey,
    chatCommand,
    modCategory,
    keyBinding
)