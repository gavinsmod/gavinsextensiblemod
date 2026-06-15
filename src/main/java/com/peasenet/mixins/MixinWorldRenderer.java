package com.peasenet.mixins;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.RenderEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
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
    void render(GraphicsResourceAllocator resourceAllocator, DeltaTracker deltaTracker, boolean renderOutline, CameraRenderState cameraState, Matrix4fc modelViewMatrix, GpuBufferSlice terrainFog, Vector4f fogColor, boolean shouldRenderSky, ChunkSectionsToRender chunkSectionsToRender, CallbackInfo ci) {
        var matrixStack = new PoseStack();
        matrixStack.mulPose(modelViewMatrix);
        var tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
        var renderEvent = RenderEvent.Companion.get(matrixStack, tickDelta);
        EventManager.getEventManager().call(renderEvent);
    }
}
