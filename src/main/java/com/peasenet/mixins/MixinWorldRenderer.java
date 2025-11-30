package com.peasenet.mixins;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.RenderEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
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

@Mixin(LevelRenderer.class)
public class MixinWorldRenderer {
    @Inject(at = @At("RETURN"), method = "renderLevel")
    void render(GraphicsResourceAllocator allocator, DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f matrix4f, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        var matrixStack = new PoseStack();
        matrixStack.mulPose(positionMatrix);
        var tickDelta = tickCounter.getGameTimeDeltaPartialTick(false);
        var renderEvent = RenderEvent.Companion.get(matrixStack, tickDelta);
        EventManager.getEventManager().call(renderEvent);
    }
}
