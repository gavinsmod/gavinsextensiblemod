package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.event.ChunkUpdateEvent;
import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.data.ChunkUpdate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "onChunkData", at = @At("TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        var chunk = GavinsModClient.Companion.getMinecraftClient().getWorld().getChunk(packet.getChunkX(), packet.getChunkZ());
        EventManager.getEventManager().call(new ChunkUpdateEvent(new ChunkUpdate(chunk)));
    }
}
