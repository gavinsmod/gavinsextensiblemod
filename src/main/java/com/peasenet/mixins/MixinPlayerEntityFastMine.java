package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import net.minecraft.entity.player.PlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntityFastMine {
    @Redirect(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z", opcode = Opcodes.PUTFIELD))    
    public void doNoClip(PlayerEntity p, boolean noClip) {    
        p.noClip = GavinsMod.isEnabled("noclip");    
        //NOTE: This is fine. It is required for fabric to work.    
        noClip = p.noClip;    
    }    
    
}
