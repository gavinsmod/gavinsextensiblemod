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

import com.llamalad7.mixinextras.sugar.Local;
import com.peasenet.util.event.CameraHurtEvent;
import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.RenderEvent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void checkAntiHurt(MatrixStack stack, float f, CallbackInfo ci) {
        CameraHurtEvent event = new CameraHurtEvent();
        EventManager.getEventManager().call(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
            opcode = Opcodes.GETFIELD,
            ordinal = 0),
            method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
    public void handleRender(RenderTickCounter tickCounter,
                             CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f2,
                             @Local(ordinal = 1) float tickDelta) {
        var matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f2);
        var renderEvent = RenderEvent.Companion.get(matrixStack, tickDelta);
        // enable line
        EventManager.getEventManager().call(renderEvent);
    }
}
