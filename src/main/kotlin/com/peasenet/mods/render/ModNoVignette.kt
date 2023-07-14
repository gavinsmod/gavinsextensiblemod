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
package com.peasenet.mods.render

import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.util.event.data.RenderOverlay
import com.peasenet.util.listeners.RenderOverlayListener
import net.minecraft.util.Identifier

/**
 * A mod that removes the vignette overlay.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
class ModNoVignette : RenderMod(
    "No Vignette",
    "gavinsmod.mod.render.novignette",
    "novignette",
), RenderOverlayListener {
    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderOverlayListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(RenderOverlayListener::class.java, this)
    }

    override fun onRenderOverlay(overlay: RenderOverlay) {
        val texture = overlay.texture
        if (texture == Identifier("textures/misc/vignette.png")) overlay.cancel()
    }
}