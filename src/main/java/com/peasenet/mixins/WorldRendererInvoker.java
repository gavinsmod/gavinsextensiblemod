package com.peasenet.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author gt3ch1
 * @version 5/18/2022
 */
@Mixin(WorldRenderer.class)
public interface WorldRendererInvoker {
    @Invoker("drawBlockOutline")
    void drawOutlineInvoker(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos pos, BlockState state);

    @Accessor("bufferBuilders")
    BufferBuilderStorage getBufferBuilder();
}
