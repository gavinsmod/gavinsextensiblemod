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

package com.peasenet.mods.esp;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.util.ChestEntityRender;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.listeners.ChestEntityRenderListener;
import net.minecraft.util.math.Box;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A mod that allows the client to see an esp (a box) around chests.
 */
public class ModChestEsp extends Mod implements ChestEntityRenderListener {
    public ModChestEsp() {
        super(Type.CHEST_ESP);
        ColorSetting colorSetting = new ColorSetting("none",
                "gavinsmod.settings.esp.chest.color");
        colorSetting.setCallback(() -> {
            GavinsMod.espConfig.setChestColor(colorSetting.getColor());
        });
        colorSetting.setColor(GavinsMod.espConfig.getChestColor());
        addSetting(colorSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(ChestEntityRenderListener.class, this);
    }

    @Override

    public void onDisable() {
        super.onDisable();
        em.unsubscribe(ChestEntityRenderListener.class, this);
    }

    @Override
    public void onEntityRender(ChestEntityRender er) {
        var box = new Box(er.entity.getPos());
        RenderUtils.drawBox(er.stack, er.buffer, box, GavinsMod.espConfig.getChestColor());
    }
}
