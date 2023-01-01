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

package com.peasenet.mods.misc;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.util.FakePlayer;
import com.peasenet.util.PlayerUtils;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.event.data.OutputPacket;
import com.peasenet.util.listeners.PacketSendListener;
import com.peasenet.util.listeners.WorldRenderListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

/**
 * A mod that allows the camera to be moved freely.
 *
 * @author GT3CH1
 * @version 12/22/2022
 */
public class ModFreecam extends Mod implements PacketSendListener, WorldRenderListener {
    FakePlayer fake;

    public ModFreecam() {
        super(Type.FREECAM);
    }

    @Override
    public void activate() {
        super.activate();
        fake = new FakePlayer();
        em.subscribe(PacketSendListener.class, this);
        em.subscribe(WorldRenderListener.class, this);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (!isActive())
            return;
        getPlayer().setVelocity(Vec3d.ZERO);
        getPlayer().airStrafingSpeed = 1;
        getPlayer().setOnGround(false);
        getPlayer().getAbilities().flying = false;

        if (getPlayer().input.sneaking)
            getPlayer().setVelocity(getPlayer().getVelocity().add(0, -1, 0));

        if (getPlayer().input.jumping)
            getPlayer().setVelocity(getPlayer().getVelocity().add(0, 1, 0));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        fake.remove();
        em.unsubscribe(PacketSendListener.class, this);
        em.unsubscribe(WorldRenderListener.class, this);
    }

    @Override
    public void onWorldRender(ClientWorld world, MatrixStack stack, BufferBuilder buffer, float delta) {
        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        var playerPos = PlayerUtils.getNewPlayerPosition(delta, camera);
        var aabb = RenderUtils.getEntityBox(delta, fake);
        RenderUtils.renderSingleLine(stack, buffer, playerPos, aabb.getCenter(), GavinsMod.tracerConfig.getPlayerColor());
        RenderUtils.drawBox(stack, buffer, aabb, GavinsMod.espConfig.getPlayerColor());
    }

    @Override
    public void onPacketSend(OutputPacket packet) {
        if (packet.getPacket() instanceof PlayerMoveC2SPacket)
            packet.cancel();
    }
}
