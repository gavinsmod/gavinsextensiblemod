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
import com.peasenet.util.EntityRender;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.listeners.EntityRenderListener;
import net.minecraft.entity.EntityType;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A mod that allows the player to see an esp (a box) around items.
 */
public class ModEntityItemEsp extends Mod implements EntityRenderListener {
    public ModEntityItemEsp() {
        super(Type.ENTITY_ITEM_ESP);
        ColorSetting colorSetting = new ColorSetting("gavinsmod.settings.esp.item.color");
        colorSetting.setCallback(() -> {
            GavinsMod.espConfig.setItemColor(colorSetting.getColor());
        });
        colorSetting.setColor(GavinsMod.espConfig.getItemColor());
        addSetting(colorSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(EntityRenderListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(EntityRenderListener.class, this);
    }

    @Override
    public void onEntityRender(EntityRender er) {
        if (er.getEntityType() != EntityType.ITEM)
            return;
        var box = RenderUtils.getEntityBox(er.delta, er.entity);
        RenderUtils.drawBox(er.stack, er.buffer, box, GavinsMod.espConfig.getItemColor());
    }
}
