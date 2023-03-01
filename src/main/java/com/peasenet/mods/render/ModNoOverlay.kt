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

package com.peasenet.mods.render;

import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.util.event.data.RenderOverlay;
import com.peasenet.util.listeners.RenderOverlayListener;
import net.minecraft.util.Identifier;

/**
 * @author gt3ch1
 * @version 01/07/2022
 * A mod that disables some overlays that are drawn on screen.
 */
public class ModNoOverlay extends Mod implements RenderOverlayListener {
    public ModNoOverlay() {
        super(Type.NO_OVERLAY);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(RenderOverlayListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(RenderOverlayListener.class, this);
    }

    @Override
    public void onRenderOverlay(RenderOverlay overlay) {
        if (overlay.getTexture().equals(new Identifier("textures/misc/powder_snow_outline.png")))
            overlay.cancel();
    }
}