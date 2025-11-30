/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.mixins;

import com.mojang.authlib.GameProfile;
import com.peasenet.mixinterface.IClientPlayerEntityAirStrafe;
import com.peasenet.util.event.AirStrafeEvent;
import com.peasenet.util.event.EventManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public abstract class MixinClientPlayerEntityAirStrafe extends AbstractClientPlayer implements IClientPlayerEntityAirStrafe {
    public MixinClientPlayerEntityAirStrafe(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public float getFlyingSpeed() {
        var evt = new AirStrafeEvent(super.getFlyingSpeed());
        EventManager.getEventManager().call(evt);
        return evt.getSpeed();
    }
}
