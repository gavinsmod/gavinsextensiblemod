/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.mods.tracer;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.util.ChestEntityRender;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.listeners.CameraBobListener;
import com.peasenet.util.listeners.ChestEntityRenderListener;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A mod that allows the player to see tracers towards chests.
 */
public class ModChestTracer extends Mod implements ChestEntityRenderListener,
        CameraBobListener {
    public ModChestTracer() {
        super(Type.CHEST_TRACER);
        ColorSetting colorSetting = new ColorSetting("gavinsmod.settings.tracer.chest.color");
        colorSetting.setCallback(() -> {
            GavinsMod.tracerConfig.setChestColor(colorSetting.getColor());
        });
        colorSetting.setColor(GavinsMod.tracerConfig.getChestColor());
        addSetting(colorSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(ChestEntityRenderListener.class, this);
        em.subscribe(CameraBobListener.class,this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(ChestEntityRenderListener.class, this);
        em.unsubscribe(CameraBobListener.class,this);
    }

    @Override
    public void onEntityRender(ChestEntityRender er) {
        RenderUtils.renderSingleLine(er.stack, er.buffer, er.playerPos, er.center, GavinsMod.tracerConfig.getChestColor());
    }

    @Override
    public void onCameraViewBob(CameraBob c) {
        c.cancel();
    }
}
