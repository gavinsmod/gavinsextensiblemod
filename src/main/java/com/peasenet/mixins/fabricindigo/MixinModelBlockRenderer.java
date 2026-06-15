package com.peasenet.mixins.fabricindigo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.peasenet.main.Mods;
import com.peasenet.mods.render.ModXray;
import com.peasenet.util.ChatCommand;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ModelBlockRenderer.class)
public abstract class MixinModelBlockRenderer {
    @WrapOperation(
            method = {
                    "tesselateFlat",
                    "tesselateAmbientOcclusion"
            },
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/renderer/block/dispatch/BlockStateModelPart;getQuads(Lnet/minecraft/core/Direction;)Ljava/util/List;"))
    private List<BakedQuad> hideUnculledQuads(BlockStateModelPart part,
                                              Direction direction, Operation<List<BakedQuad>> original,
                                              BlockQuadOutput output, float x, float y, float z,
                                              List<BlockStateModelPart> list, BlockAndTintGetter level,
                                              BlockState state, BlockPos pos) {
        var drawSide = ModXray.Companion.shouldDrawFace(state);
        if (!drawSide)
            return List.of();
        return original.call(part, direction);
    }
}
