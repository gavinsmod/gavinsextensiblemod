/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
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
import com.peasenet.util.event.data.EntityRender;
import com.peasenet.util.listeners.EntityRenderNameListener;
import net.minecraft.entity.LivingEntity;

/**
 * @author gt3ch1
 * @version 12/31/2022
 * A mod that shows entity's health as a tag above their head.
 */
public class ModHealthTag extends Mod implements EntityRenderNameListener {
    public ModHealthTag() {
        super(Type.MOD_HPTAG);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(EntityRenderNameListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(EntityRenderNameListener.class, this);
    }

    @Override
    public void onEntityRender(EntityRender er) {
        var entity = er.entity;
        var matrices = er.stack;
        var textRenderer = getClient().getTextRenderer();
        var d = getClient().getEntityRenderDispatcher().getSquaredDistanceToCamera(entity);
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        if (d > 1024)
            return;
        var currentHealth = livingEntity.getHealth();
        var text = currentHealth + " HP";
        boolean bl = !entity.isSneaky();
        float f = entity.getHeight() + 0.5F;

        matrices.push();
        matrices.translate(0.0D, f, 0.0D);
        matrices.multiply(getClient().getEntityRenderDispatcher().getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        var matrix4f = matrices.peek().getPositionMatrix();
        float g = getClient().getOptions().getTextBackgroundOpacity(0.5f);
        int j = (int) (g * 255.0F) << 24;
        float h = (float) (-textRenderer.getWidth(text) / 2);
        int color = 0x00ff00;
        var percentHealth = (double) (livingEntity.getHealth() / livingEntity.getMaxHealth());
        if (percentHealth < 0.75)
            color = 0xffff00;
        if (percentHealth < 0.5)
            color = 0xffa500;
        if (percentHealth < 0.25)
            color = 0xff0000;
        textRenderer.draw(text, h, (float) 0, color, false, matrix4f, er.vertexConsumers, bl, j, er.light);
        if (bl) {
            textRenderer.draw(text, h, (float) 0, color, false, matrix4f, er.vertexConsumers, false, 0, er.light);
        }

        matrices.pop();
    }
}
