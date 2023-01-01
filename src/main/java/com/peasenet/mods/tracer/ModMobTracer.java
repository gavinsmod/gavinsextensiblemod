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

package com.peasenet.mods.tracer;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.event.data.CameraBob;
import com.peasenet.util.event.data.EntityRender;
import com.peasenet.util.listeners.CameraBobListener;
import com.peasenet.util.listeners.EntityRenderListener;
import net.minecraft.entity.mob.MobEntity;

/**
 * @author gt3ch1
 * @version 12/31/2022
 * A mod that allows the client to see lines, called tracers, towards mobs.
 */
public class ModMobTracer extends Mod implements EntityRenderListener, CameraBobListener {
    public ModMobTracer() {
        super(Type.MOB_TRACER);
        ColorSetting peacefulColor = new ColorSetting("gavinsmod.settings.tracer.mob.peaceful.color");
        peacefulColor.setCallback(() -> tracerConfig.setPeacefulMobColor(peacefulColor.getColor()));
        peacefulColor.setColor(GavinsMod.tracerConfig.getPeacefulMobColor());


        ColorSetting hostileColor = new ColorSetting("gavinsmod.settings.tracer.mob.hostile.color");
        hostileColor.setCallback(() -> tracerConfig.setHostileMobColor(hostileColor.getColor()));
        hostileColor.setColor(GavinsMod.tracerConfig.getHostileMobColor());

        ToggleSetting hostile = new ToggleSetting("gavinsmod.settings.tracer.mob.hostile");
        hostile.setCallback(() -> tracerConfig.setShowHostileMobs(hostile.getValue()));
        hostile.setValue(GavinsMod.tracerConfig.isShowHostileMobs());

        ToggleSetting peaceful = new ToggleSetting("gavinsmod.settings.tracer.mob.peaceful");
        peaceful.setCallback(() -> tracerConfig.setShowPeacefulMobs(peaceful.getValue()));
        peaceful.setValue(GavinsMod.tracerConfig.isShowPeacefulMobs());

        addSetting(hostileColor);
        addSetting(peacefulColor);
        addSetting(hostile);
        addSetting(peaceful);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(EntityRenderListener.class, this);
        em.subscribe(CameraBobListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(EntityRenderListener.class, this);
        em.unsubscribe(CameraBobListener.class, this);
    }

    @Override
    public void onEntityRender(EntityRender er) {
        // check if entity is a mob
        var entity = er.entity;
        var stack = er.stack;
        var buffer = er.buffer;
        var center = er.center;
        var playerPos = er.playerPos;
        if (!(entity instanceof MobEntity))
            return;
        if (er.getEntityType().getSpawnGroup().isPeaceful() && tracerConfig.isShowPeacefulMobs()) {
            RenderUtils.renderSingleLine(stack, buffer, playerPos, center, tracerConfig.getPeacefulMobColor(), tracerConfig.getAlpha());
        } else if (!er.getEntityType().getSpawnGroup().isPeaceful() && tracerConfig.isShowHostileMobs()) {
            RenderUtils.renderSingleLine(stack, buffer, playerPos, center, tracerConfig.getHostileMobColor(), tracerConfig.getAlpha());
        }
    }

    @Override
    public void onCameraViewBob(CameraBob c) {
        c.cancel();
    }
}
