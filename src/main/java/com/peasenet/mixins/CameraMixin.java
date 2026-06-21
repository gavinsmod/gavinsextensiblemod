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

import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.mods.misc.ModFreeCam;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.level.material.FogType;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private boolean detached;
    @Inject(at = @At("HEAD"), method = "getFluidInCamera", cancellable = true)
    public void getSubmersionType(CallbackInfoReturnable<FogType> cir) {
        if (Mods.isActive(ChatCommand.NoOverlay)) cir.setReturnValue(FogType.NONE);
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;alignWithEntity(F)V", shift = At.Shift.AFTER))
    public void update(DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!Mods.isActive(ChatCommand.FreeCam))
            return;
        var ticks = deltaTracker.getGameTimeDeltaPartialTick(true);
        detached = true;
        ModFreeCam freeCam = Mods.getMod(ChatCommand.FreeCam) ;
        setPosition(freeCam.getCameraPos(ticks));
        setRotation(freeCam.getCamYaw(), freeCam.getCamPitch());
    }

    @Shadow
    protected abstract void setPosition(Vec3 position);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);
}

