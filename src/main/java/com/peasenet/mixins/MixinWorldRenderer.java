package com.peasenet.mixins;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.RenderEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *
 * @author GT3CH1
 * @version 11-24-2025
 * @since 11-24-2025
 */

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Inject(at = @At("RETURN"), method = "render")
    void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f matrix4f, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        var matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(positionMatrix);
        var tickDelta = tickCounter.getTickProgress(false);
        var renderEvent = RenderEvent.Companion.get(matrixStack, tickDelta);
        EventManager.getEventManager().call(renderEvent);
    }
}
