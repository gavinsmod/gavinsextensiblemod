
package com.peasenet.mixins;

import com.peasenet.util.event.BlockUpdateEvent;
import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.data.BlockUpdate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public class MixinWorldChunk {

    @Inject(method = "setBlockState", at = @At("TAIL"))
    private void onSetBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        var data = new BlockUpdate(pos, state, cir.getReturnValue());
        var blockUpdateEvent = new BlockUpdateEvent(data);
        EventManager.getEventManager().call(blockUpdateEvent);
    }
}
