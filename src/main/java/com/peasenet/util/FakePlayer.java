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

package com.peasenet.util;

import com.peasenet.main.GavinsModClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayer extends OtherClientPlayerEntity {
    private final ClientWorld world = GavinsModClient.getMinecraftClient().getWorld();
    private final ClientPlayerEntity player = GavinsModClient.getMinecraftClient().getPlayer();

    public FakePlayer() {
        super(GavinsModClient.getMinecraftClient().getWorld(), GavinsModClient.getPlayer().getGameProfile());
//        uuid = java.util.UUID.randomUUID();
//        this.setUuid(uuid);
        copyPositionAndRotation(player);
        getInventory().clone(player.getInventory());
        DataTracker fromTracker = player.getDataTracker();
        DataTracker toTracker = this.getDataTracker();
        Byte playerModel = fromTracker.get(PlayerEntity.PLAYER_MODEL_PARTS);
        toTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
        headYaw = GavinsModClient.getPlayer().getHeadYaw();
        bodyYaw = GavinsModClient.getPlayer().getBodyYaw();
        getInventory().clone(player.getInventory());
        world.addEntity(getId(), this);
    }

    public void remove() {
        // move player back to original position
        player.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());
        discard();
    }
}
